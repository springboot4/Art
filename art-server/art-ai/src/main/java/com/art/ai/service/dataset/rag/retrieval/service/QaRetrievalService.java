package com.art.ai.service.dataset.rag.retrieval.service;

import com.art.ai.dao.dataobject.AiQaHitLogsDO;
import com.art.ai.dao.dataobject.AiQaPairsDO;
import com.art.ai.dao.dataobject.AiQaSimilarQuestionsDO;
import com.art.ai.manager.AiQaHitLogsManager;
import com.art.ai.manager.AiQaPairsManager;
import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.qa.util.QaHashUtil;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.ai.service.qa.QaVectorizationService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.art.ai.service.qa.QaVectorizationService.DATASET_ID_KEY;
import static com.art.ai.service.qa.QaVectorizationService.QA_ID_KEY;

/**
 * QA问答对检索服务
 *
 * @author fxz
 * @since 2025/10/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaRetrievalService {

	private final AiQaPairsManager qaPairsManager;

	private final AiQaHitLogsManager hitLogsManager;

	private static final double EXACT_MATCH_CONFIDENCE = 1.0;

	private static final double HIGH_CONFIDENCE_THRESHOLD = 0.75;

	/**
	 * QA检索主入口
	 */
	public List<RetrievalResult> retrieve(String query, Long datasetId, EmbeddingModel embeddingModel,
			EmbeddingStore<TextSegment> embeddingStore, PipelineConfig.QaRetrievalConfig qaConfig) {
		long startTime = System.currentTimeMillis();

		try {
			List<RetrievalResult> results = new ArrayList<>();

			// 精确匹配
			List<RetrievalResult> exactResults = exactMatch(query, datasetId);
			if (!exactResults.isEmpty()) {
				results.addAll(exactResults);
				if (exactResults.get(0).getScore().doubleValue() >= HIGH_CONFIDENCE_THRESHOLD) {
					logHit(exactResults.get(0), query, "exact", startTime);
					return results;
				}
			}

			// 语义向量检索
			if (embeddingModel != null && embeddingStore != null) {
				List<RetrievalResult> semanticResults = semanticMatch(query, datasetId, embeddingModel, embeddingStore,
						qaConfig);
				results.addAll(semanticResults);

				// 如果语义匹配有高分结果,可以提前返回
				if (!semanticResults.isEmpty()
						&& semanticResults.get(0).getScore().doubleValue() >= HIGH_CONFIDENCE_THRESHOLD) {
					logHit(semanticResults.get(0), query, "semantic", startTime);
					return deduplicateResults(results);
				}
			}

			return deduplicateResults(results);
		}
		catch (Exception e) {
			log.error("QA检索失败: query={}, datasetId={}", query, datasetId, e);
			return List.of();
		}
	}

	/**
	 * 精确匹配
	 */
	private List<RetrievalResult> exactMatch(String query, Long datasetId) {
		try {
			String queryHash = QaHashUtil.calculateQuestionHash(query);
			if (queryHash == null) {
				return List.of();
			}

			// Step 1: 查询主表
			AiQaPairsDO mainResult = qaPairsManager.findByQuestionHash(queryHash, datasetId);
			if (mainResult != null) {
				return List.of(convertToRetrievalResult(mainResult, EXACT_MATCH_CONFIDENCE, "exact"));
			}

			// Step 2: 查询相似问题表
			List<AiQaSimilarQuestionsDO> similarResults = qaPairsManager.findSimilarByHash(queryHash);
			if (!similarResults.isEmpty()) {
				Long qaPairId = similarResults.get(0).getQaPairId();
				AiQaPairsDO qaPair = qaPairsManager.findById(qaPairId);
				if (qaPair != null) {
					return List.of(convertToRetrievalResult(qaPair, EXACT_MATCH_CONFIDENCE, "exact_similar"));
				}
			}

			return List.of();
		}
		catch (Exception e) {
			log.error("精确匹配失败: query={}", query, e);
			return List.of();
		}
	}

	/**
	 * 语义向量检索
	 */
	private List<RetrievalResult> semanticMatch(String query, Long datasetId, EmbeddingModel embeddingModel,
			EmbeddingStore<TextSegment> embeddingStore, PipelineConfig.QaRetrievalConfig qaConfig) {
		try {
			Embedding queryEmbedding = embeddingModel.embed(query).content();

			int maxResults = qaConfig != null && qaConfig.getTopK() != null ? qaConfig.getTopK() : 10;
			double minScore = qaConfig != null && qaConfig.getMinScore() != null ? qaConfig.getMinScore() : 0.6;

			EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
				.queryEmbedding(queryEmbedding)
				.minScore(minScore)
				.maxResults(maxResults)
				.filter(new IsEqualTo(QaVectorizationService.QA_TYPE_KEY, "qa")
					.and(new IsEqualTo(DATASET_ID_KEY, datasetId)))
				.build();

			List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(searchRequest).matches();

			return matches.stream().map(match -> {
				String qaIdStr = match.embedded().metadata().getString(QA_ID_KEY);
				if (qaIdStr != null) {
					AiQaPairsDO qaPair = qaPairsManager.findById(Long.valueOf(qaIdStr));
					if (qaPair != null && qaPair.getEnabled()) {
						double confidence = calculateConfidence(qaPair, match.score());
						return convertToRetrievalResult(qaPair, confidence, "semantic");
					}
				}
				return null;
			}).filter(java.util.Objects::nonNull).collect(Collectors.toList());
		}
		catch (Exception e) {
			log.error("语义检索失败: query={}", query, e);
			return List.of();
		}
	}

	/**
	 * 计算综合置信度 考虑: 匹配分数、优先级、时效性、历史命中
	 */
	private double calculateConfidence(AiQaPairsDO qaPair, double matchScore) {
		// 优先级权重 (1-5级, 每级+5%)
		double priorityWeight = 1.0 + (qaPair.getPriority() - 1) * 0.05;

		// 时效性 (最近更新的QA更可信)
		double freshness = calculateFreshness(qaPair.getUpdateTime());

		// 命中次数加成 (对数缩放)
		double hitBoost = 1.0 + Math.min(Math.log10(qaPair.getHitCount() + 1) * 0.05, 0.15);

		// 综合计算
		double confidence = matchScore * priorityWeight * freshness * hitBoost;

		return Math.max(0.0, Math.min(1.0, confidence));
	}

	/**
	 * 计算时效性衰减
	 */
	private double calculateFreshness(LocalDateTime updateTime) {
		if (updateTime == null) {
			return 0.8;
		}

		long daysSinceUpdate = java.time.Duration.between(updateTime, LocalDateTime.now()).toDays();

		if (daysSinceUpdate <= 90) {
			return 1.0;
		}
		else if (daysSinceUpdate <= 180) {
			return 0.95;
		}
		else if (daysSinceUpdate <= 365) {
			return 0.90;
		}
		else {
			return 0.80;
		}
	}

	/**
	 * 去重: 同一qa_id保留最高分
	 */
	private List<RetrievalResult> deduplicateResults(List<RetrievalResult> results) {
		Map<String, RetrievalResult> uniqueResults = new LinkedHashMap<>();

		for (RetrievalResult result : results) {
			String qaId = (String) result.getMetadata().get("qa_id");
			if (qaId != null) {
				if (!uniqueResults.containsKey(qaId)
						|| uniqueResults.get(qaId).getScore().compareTo(result.getScore()) < 0) {
					uniqueResults.put(qaId, result);
				}
			}
		}

		return new ArrayList<>(uniqueResults.values());
	}

	/**
	 * 转换为RetrievalResult
	 */
	private RetrievalResult convertToRetrievalResult(AiQaPairsDO qaPair, double confidence, String matchType) {
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("qa_id", qaPair.getId().toString());
		metadata.put("question", qaPair.getQuestion());
		metadata.put("match_type", matchType);
		metadata.put("confidence", confidence);
		metadata.put("priority", qaPair.getPriority());
		metadata.put("dataset_id", qaPair.getDatasetId());

		// 更新命中次数
		CompletableFuture.runAsync(() -> updateHitCount(qaPair.getId()));

		return RetrievalResult.builder()
			.documentId(qaPair.getId().toString())
			.segmentId(qaPair.getId().toString())
			.content(qaPair.getAnswer())
			.score(BigDecimal.valueOf(confidence).setScale(2, RoundingMode.HALF_UP))
			.retrievalType(RetrievalType.QA)
			.metadata(metadata)
			.build();
	}

	/**
	 * 更新命中次数
	 */
	private void updateHitCount(Long qaPairId) {
		try {
			qaPairsManager.updateHitCount(qaPairId);
		}
		catch (Exception e) {
			log.error("更新命中次数失败: qaPairId={}", qaPairId, e);
		}
	}

	/**
	 * 记录命中日志
	 */
	private void logHit(RetrievalResult result, String query, String matchType, long startTime) {
		try {
			String qaId = (String) result.getMetadata().get("qa_id");
			if (qaId == null) {
				return;
			}

			AiQaHitLogsDO log = new AiQaHitLogsDO();
			log.setQaPairId(Long.valueOf(qaId));
			log.setUserQuery(query);
			log.setMatchType(matchType);
			log.setMatchScore(result.getScore());
			log.setResponseTimeMs((int) (System.currentTimeMillis() - startTime));

			hitLogsManager.insert(log);
		}
		catch (Exception e) {
			log.error("记录命中日志失败", e);
		}
	}

}
