package com.art.ai.service.dataset.rag.rerank.impl;

import com.art.ai.service.dataset.rag.rerank.Reranker;
import com.art.ai.service.dataset.rag.rerank.RerankerConfig;
import com.art.ai.service.dataset.rag.rerank.RerankerType;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于MMR的多样性重排序器<br/>
 * MMR (Maximal Marginal Relevance) 算法平衡相关性和多样性<br/>
 * MMR 分数 = `λ` * (相关性) - (1 - `λ`) * (最大相似度)<br/>
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Component
public class DiversityReranker implements Reranker {

	/**
	 * Embedding缓存,避免重复计算 Key: 文本内容, Value: Embedding向量
	 */
	private Map<String, Embedding> embeddingCache = new ConcurrentHashMap<>();

	/**
	 * 用于计算文本Embedding的模型(可选)
	 */
	private EmbeddingModel embeddingModel;

	@Override
	public List<RetrievalResult> rerank(String query, List<RetrievalResult> results, RerankerConfig config) {
		if (results.isEmpty()) {
			return results;
		}

		try {
			RerankerConfig.DiversityRerankerConfig diversityConfig = config.getDiversityRerankerConfig();
			if (diversityConfig == null) {
				diversityConfig = getDefaultDiversityConfig();
			}
			embeddingModel = config.getDiversityRerankerConfig().getEmbeddingModel();
			embeddingCache = new HashMap<>();

			return performMMRReranking(query, results, diversityConfig, config);
		}
		catch (Exception e) {
			log.error("多样性重排序失败", e);
			return results;
		}
	}

	/**
	 * 执行MMR重排序
	 */
	private List<RetrievalResult> performMMRReranking(String query, List<RetrievalResult> results,
			RerankerConfig.DiversityRerankerConfig config, RerankerConfig globalConfig) {
		double lambda = config.getLambda() != null ? config.getLambda() : 0.7;
		int topK = globalConfig.getTopK() != null ? globalConfig.getTopK() : results.size();

		if (results.isEmpty() || topK <= 0) {
			return new ArrayList<>();
		}

		long startTime = System.currentTimeMillis();

		// 批量预计算所有Embedding
		batchPrecomputeEmbeddings(results);

		// 预计算相似度矩阵
		double[][] similarityMatrix = precomputeSimilarityMatrix(results);

		long prepTime = System.currentTimeMillis() - startTime;
		log.debug("相似度矩阵预计算完成: docs={}, time={}ms", results.size(), prepTime);

		// MMR
		List<RetrievalResult> selected = performMMRWithMatrix(results, similarityMatrix, lambda, topK);

		long totalTime = System.currentTimeMillis() - startTime;
		log.info("MMR重排序完成: input={}, output={}, duration={}ms", results.size(), selected.size(), totalTime);

		return selected;
	}

	/**
	 * 批量预计算所有文档的Embedding
	 */
	private void batchPrecomputeEmbeddings(List<RetrievalResult> results) {
		if (embeddingModel == null) {
			return;
		}

		// 筛选未缓存的内容
		List<TextSegment> uncachedContents = results.stream()
			.map(RetrievalResult::getContent)
			.filter(content -> !embeddingCache.containsKey(content))
			.distinct()
			.map(TextSegment::from)
			.toList();

		if (uncachedContents.isEmpty()) {
			log.debug("所有Embedding已缓存,无需重新计算");
			return;
		}

		try {
			long start = System.currentTimeMillis();
			List<Embedding> embeddings = embeddingModel.embedAll(uncachedContents).content();

			// 缓存结果
			for (int i = 0; i < uncachedContents.size(); i++) {
				embeddingCache.put(uncachedContents.get(i).text(), embeddings.get(i));
			}

			long duration = System.currentTimeMillis() - start;
			log.debug("批量Embedding计算: count={}, duration={}ms", uncachedContents.size(), duration);
		}
		catch (Exception e) {
			log.warn("批量Embedding计算失败,将逐个计算: {}", e.getMessage());
		}
	}

	/**
	 * 预计算相似度矩阵
	 */
	private double[][] precomputeSimilarityMatrix(List<RetrievalResult> results) {
		int n = results.size();
		double[][] matrix = new double[n][n];

		for (int i = 0; i < n; i++) {
			matrix[i][i] = 1.0;

			for (int j = i + 1; j < n; j++) {
				double similarity = calculateContentSimilarity(results.get(i).getContent(),
						results.get(j).getContent());

				matrix[i][j] = similarity;
				matrix[j][i] = similarity;
			}
		}

		return matrix;
	}

	/**
	 * 使用预计算矩阵的MMR选择算法
	 * </p>
	 */
	private List<RetrievalResult> performMMRWithMatrix(List<RetrievalResult> results, double[][] similarityMatrix,
			double lambda, int topK) {

		List<RetrievalResult> selected = new ArrayList<>();
		boolean[] selectedFlags = new boolean[results.size()];

		// 迭代选择topK个文档
		for (int round = 0; round < topK && round < results.size(); round++) {
			int bestIdx = -1;
			double bestMmrScore = Double.NEGATIVE_INFINITY;

			// 遍历所有未选中的候选
			for (int i = 0; i < results.size(); i++) {
				if (selectedFlags[i]) {
					continue;
				}

				double relevance = results.get(i).getScore().doubleValue();

				// 计算与已选文档的最大相似度
				double maxSimilarity = 0.0;
				for (int j = 0; j < results.size(); j++) {
					if (selectedFlags[j]) {
						maxSimilarity = Math.max(maxSimilarity, similarityMatrix[i][j]);
					}
				}

				// MMR分数: λ*相关性 - (1-λ)*最大相似度
				double mmrScore = lambda * relevance - (1 - lambda) * maxSimilarity;

				if (mmrScore > bestMmrScore) {
					bestMmrScore = mmrScore;
					bestIdx = i;
				}
			}

			// 选中最佳候选
			if (bestIdx != -1) {
				selectedFlags[bestIdx] = true;
				RetrievalResult selectedResult = results.get(bestIdx);

				// 保存原始分数
				double originalScore = selectedResult.getScore().doubleValue();

				// 更新元数据
				selectedResult.setScore(BigDecimal.valueOf(bestMmrScore));
				selectedResult.getMetadata().put("mmr_position", selected.size() + 1);
				selectedResult.getMetadata().put("mmr_score", bestMmrScore);
				selectedResult.getMetadata().put("original_score", originalScore);

				selected.add(selectedResult);
			}
		}

		return selected;
	}

	/**
	 * 计算内容相似度
	 */
	private double calculateContentSimilarity(String content1, String content2) {
		if (content1 == null || content2 == null) {
			return 0.0;
		}

		return calculateEmbeddingSimilarity(content1, content2);
	}

	/**
	 * 计算Embedding余弦相似度
	 */
	private double calculateEmbeddingSimilarity(String content1, String content2) {
		try {
			Embedding embedding1 = getOrComputeEmbedding(content1);
			Embedding embedding2 = getOrComputeEmbedding(content2);

			double similarity = CosineSimilarity.between(embedding1, embedding2);

			// Cosine相似度范围[-1, 1],归一化到[0, 1]
			return (similarity + 1.0) / 2.0;
		}
		catch (Exception e) {
			log.warn("Embedding相似度计算失败: {}", e.getMessage());
			return 0.0;
		}
	}

	/**
	 * 获取或计算Embedding (带缓存)
	 */
	private Embedding getOrComputeEmbedding(String text) {
		return embeddingCache.computeIfAbsent(text, t -> {
			try {
				return embeddingModel.embed(t).content();
			}
			catch (Exception e) {
				throw new RuntimeException("Embedding计算失败: " + e.getMessage(), e);
			}
		});
	}

	/**
	 * 获取默认多样性配置
	 */
	private RerankerConfig.DiversityRerankerConfig getDefaultDiversityConfig() {
		return RerankerConfig.DiversityRerankerConfig.builder()
			// 70%相关性，30%多样性
			.lambda(0.7)
			// 在前50个候选中选择
			.windowSize(50)
			.build();
	}

	@Override
	public RerankerType getType() {
		return RerankerType.DIVERSITY;
	}

	@Override
	public boolean supports(RerankerConfig config) {
		return config.getType() == getType();
	}

}