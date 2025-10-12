package com.art.ai.service.dataset.rag.pipeline.impl;

import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.service.dataset.rag.fusion.FusionService;
import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.pipeline.RetrievalPipeline;
import com.art.ai.service.dataset.rag.rerank.Reranker;
import com.art.ai.service.dataset.rag.rerank.RerankerConfig;
import com.art.ai.service.dataset.rag.rerank.RerankerFactory;
import com.art.ai.service.dataset.rag.rerank.RerankerType;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResponse;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.ai.service.dataset.rag.retrieval.service.GraphRetrievalService;
import com.art.ai.service.dataset.rag.retrieval.service.HybridRetrievalService;
import com.art.ai.service.dataset.rag.retrieval.service.QaRetrievalService;
import com.art.ai.service.dataset.rag.retrieval.service.VectorRetrievalService;
import com.art.ai.service.dataset.service.AiDatasetsService;
import com.art.ai.service.document.impl.AiDocumentsServiceImpl;
import com.art.ai.service.model.runtime.AiModelRuntimeService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 默认召回管道实现
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRetrievalPipeline implements RetrievalPipeline {

	private final QaRetrievalService qaRetrievalService;

	private final VectorRetrievalService vectorRetrievalService;

	private final GraphRetrievalService graphRetrievalService;

	private final HybridRetrievalService hybridRetrievalService;

	private final RerankerFactory rerankerFactory;

	private final FusionService fusionService;

	private final AiDocumentsServiceImpl aiDocumentsService;

	private final AiDatasetsService aiDatasetsService;

	private final AiModelRuntimeService aiModelRuntimeService;

	@Override
	public RetrievalResponse execute(RetrievalRequest request) {
		long startTime = System.currentTimeMillis();

		try {
			PipelineConfig config = getPipelineConfig(request);

			Map<String, Object> debugInfo = new HashMap<>();
			debugInfo.put("pipeline_start", startTime);
			debugInfo.put("request", request);

			// 1. 召回阶段 - 并行执行多个召回器
			long retrievalStart = System.currentTimeMillis();
			List<RetrievalResult> retrievalResults = performRetrieval(request, config);
			long retrievalTime = System.currentTimeMillis() - retrievalStart;
			debugInfo.put("retrieval_time_ms", retrievalTime);
			debugInfo.put("initial_results_count", retrievalResults.size());

			// 2. 融合阶段 - 合并不同召回器的结果
			long fusionStart = System.currentTimeMillis();
			List<RetrievalResult> fusedResults = performFusion(retrievalResults, config);
			long fusionTime = System.currentTimeMillis() - fusionStart;
			debugInfo.put("fusion_time_ms", fusionTime);
			debugInfo.put("fused_results_count", fusedResults.size());

			// 3. 重排序阶段 - 应用多种重排序策略
			long rerankStart = System.currentTimeMillis();
			List<RetrievalResult> rerankedResults = performReranking(request.getQuery(), fusedResults, config);
			long rerankTime = System.currentTimeMillis() - rerankStart;
			debugInfo.put("rerank_time_ms", rerankTime);
			debugInfo.put("reranked_results_count", rerankedResults.size());

			// 4. 后处理阶段 - 去重、过滤、限制数量
			long postProcessStart = System.currentTimeMillis();
			List<RetrievalResult> finalResults = performPostProcessing(rerankedResults, config);
			long postProcessTime = System.currentTimeMillis() - postProcessStart;
			debugInfo.put("post_process_time_ms", postProcessTime);
			debugInfo.put("final_results_count", finalResults.size());

			// 5. 构建响应
			long totalTime = System.currentTimeMillis() - startTime;
			Map<RetrievalType, Integer> typeCount = calculateTypeCount(finalResults);

			return RetrievalResponse.builder()
				.query(request.getQuery())
				.results(finalResults)
				.totalCount(finalResults.size())
				.costTime(totalTime)
				.typeCount(typeCount)
				.debugInfo(debugInfo)
				.build();
		}
		catch (Exception e) {
			log.error("召回管道执行失败", e);
			throw new RuntimeException("召回管道执行失败: " + e.getMessage(), e);
		}
	}

	private PipelineConfig getPipelineConfig(RetrievalRequest request) {
        return getDefaultConfig(request.getAllDatasetIds().get(0));
	}

	/**
	 * 执行召回阶段
	 */
	private List<RetrievalResult> performRetrieval(RetrievalRequest request, PipelineConfig config) {
		List<RetrievalResult> allResults;

		if (config.getEnableParallel() != null && config.getEnableParallel()) {
			allResults = performParallelRetrieval(request, config);
		}
		else {
			allResults = performSequentialRetrieval(request, config);
		}

		return allResults;
	}

	/**
	 * 并行召回
	 */
	private List<RetrievalResult> performParallelRetrieval(RetrievalRequest request, PipelineConfig config) {
		List<CompletableFuture<List<RetrievalResult>>> futures = new ArrayList<>();

		for (RetrievalType type : request.getRetrievalTypes()) {
			PipelineConfig.RetrieverConfig retrieverConfig = getRetrieverConfig(type, config);
			if (retrieverConfig.getEnabled() != null && retrieverConfig.getEnabled()) {
				CompletableFuture<List<RetrievalResult>> future = CompletableFuture
					.supplyAsync(() -> executeRetriever(type, request, retrieverConfig));
				futures.add(future);
			}
		}

		// 等待所有召回完成
		List<RetrievalResult> allResults = new ArrayList<>();

		try {
			CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
			allOf.get();

			for (CompletableFuture<List<RetrievalResult>> future : futures) {
				allResults.addAll(future.get());
			}
		}
		catch (Exception e) {
			log.error("并行召回失败", e);
			return performSequentialRetrieval(request, config);
		}

		return allResults;
	}

	/**
	 * 串行召回
	 */
	private List<RetrievalResult> performSequentialRetrieval(RetrievalRequest request, PipelineConfig config) {
		List<RetrievalResult> allResults = new ArrayList<>();

		for (RetrievalType type : request.getRetrievalTypes()) {
			PipelineConfig.RetrieverConfig retrieverConfig = getRetrieverConfig(type, config);
			if (retrieverConfig.getEnabled() != null && retrieverConfig.getEnabled()) {
				try {
					List<RetrievalResult> typeResults = executeRetriever(type, request, retrieverConfig);
					allResults.addAll(typeResults);
				}
				catch (Exception e) {
					log.error("召回器执行失败: type={}", type, e);
				}
			}
		}

		return allResults;
	}

	/**
	 * 执行特定类型的召回器
	 */
	private List<RetrievalResult> executeRetriever(RetrievalType type, RetrievalRequest request,
			PipelineConfig.RetrieverConfig retrieverConfig) {
		try {
			List<Long> datasetIds = request.getAllDatasetIds();
			if (datasetIds.isEmpty()) {
				return Collections.emptyList();
			}

			List<CompletableFuture<List<RetrievalResult>>> futures = datasetIds.stream()
				.map(datasetId -> CompletableFuture.supplyAsync(() -> {
					try {
						return executeSingleDatasetRetriever(type, request.getQuery(), datasetId, retrieverConfig);
					}
					catch (Exception e) {
						log.error("数据集召回失败: type={}, datasetId={}", type, datasetId, e);
						return Collections.<RetrievalResult>emptyList();
					}
				}))
				.toList();

			List<RetrievalResult> allResults = new ArrayList<>();
			try {
				CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
				for (CompletableFuture<List<RetrievalResult>> future : futures) {
					allResults.addAll(future.get());
				}
			}
			catch (Exception e) {
				log.error("并发召回失败,降级为串行执行: type={}", type, e);
			}

			return allResults;
		}
		catch (Exception e) {
			log.error("召回器执行异常: type={}", type, e);
			return Collections.emptyList();
		}
	}

	/**
	 * 执行召回
	 */
	private List<RetrievalResult> executeSingleDatasetRetriever(RetrievalType type, String query, Long datasetId,
			PipelineConfig.RetrieverConfig retrieverConfig) {
		return switch (type) {
			case QA -> qaRetrievalService.retrieve(query, datasetId, getEmbeddingModel(datasetId),
					getEmbeddingStore(datasetId), retrieverConfig.getQaConfig());

			case VECTOR -> vectorRetrievalService.retrieve(query, datasetId, getEmbeddingModel(datasetId),
					getEmbeddingStore(datasetId), retrieverConfig.getVectorConfig());

			case GRAPH -> graphRetrievalService.retrieve(query, datasetId, getChatModel(datasetId),
					retrieverConfig.getGraphConfig());

			case HYBRID -> hybridRetrievalService.retrieve(query, datasetId, getEmbeddingModel(datasetId),
					getEmbeddingStore(datasetId), getChatModel(datasetId), retrieverConfig);
		};
	}

	/**
	 * 执行融合阶段
	 */
	private List<RetrievalResult> performFusion(List<RetrievalResult> results, PipelineConfig config) {
		PipelineConfig.FusionConfig fusionConfig = config.getFusionConfig();
		if (fusionConfig == null) {
			return results;
		}

		return fusionService.fuse(results, fusionConfig);
	}

	/**
	 * 执行重排序阶段
	 */
	private List<RetrievalResult> performReranking(String query, List<RetrievalResult> results, PipelineConfig config) {
		List<RerankerConfig> rerankerConfigs = config.getRerankerConfigs();
		if (CollectionUtils.isEmpty(rerankerConfigs)) {
			return results;
		}

		List<RetrievalResult> currentResults = new ArrayList<>(results);
		for (RerankerConfig rerankerConfig : rerankerConfigs) {
			if (rerankerConfig.getEnabled() != null && rerankerConfig.getEnabled()) {
				try {
					Reranker reranker = rerankerFactory.getReranker(rerankerConfig);
					currentResults = reranker.rerank(query, currentResults, rerankerConfig);
				}
				catch (Exception e) {
					log.error("重排序失败: type={}", rerankerConfig.getType(), e);
				}
			}
		}

		return currentResults;
	}

	/**
	 * 执行后处理阶段
	 */
	private List<RetrievalResult> performPostProcessing(List<RetrievalResult> results, PipelineConfig config) {
		PipelineConfig.PostProcessConfig postConfig = config.getPostProcessConfig();
		if (postConfig == null) {
			return results;
		}

		List<RetrievalResult> processedResults = new ArrayList<>(results);

		// 1. 去重
		if (postConfig.getDeduplicationStrategy() != null) {
			processedResults = performDeduplication(processedResults, postConfig);
		}

		// 2. 分数过滤
		if (postConfig.getMinScoreFilter() != null) {
			processedResults = processedResults.stream()
				.filter(r -> r.getScore().doubleValue() >= postConfig.getMinScoreFilter())
				.collect(Collectors.toList());
		}

		// 3. 限制数量
		int finalTopK = postConfig.getFinalTopK() != null ? postConfig.getFinalTopK() : 10;
		processedResults = processedResults.stream().limit(finalTopK).collect(Collectors.toList());

		return processedResults;
	}

	/**
	 * 执行去重
	 */
	private List<RetrievalResult> performDeduplication(List<RetrievalResult> results,
			PipelineConfig.PostProcessConfig config) {
		String strategy = config.getDeduplicationStrategy();

		return switch (strategy.toLowerCase()) {
			case "content" -> deduplicateByContent(results);
			case "segment_id" -> deduplicateByDocumentId(results);
			default -> results;
		};
	}

	/**
	 * 按内容去重
	 */
	private List<RetrievalResult> deduplicateByContent(List<RetrievalResult> results) {
		Map<String, RetrievalResult> uniqueResults = new LinkedHashMap<>();

		for (RetrievalResult result : results) {
			String key = String.valueOf(result.getContent().trim().toLowerCase().hashCode());
			if (!uniqueResults.containsKey(key) || uniqueResults.get(key).getScore().compareTo(result.getScore()) < 0) {
				uniqueResults.put(key, result);
			}
		}

		return new ArrayList<>(uniqueResults.values());
	}

	/**
	 * 按segmentId去重
	 */
	private List<RetrievalResult> deduplicateByDocumentId(List<RetrievalResult> results) {
		Map<String, RetrievalResult> uniqueResults = new LinkedHashMap<>();

		for (RetrievalResult result : results) {
			String key = result.getSegmentId() != null ? result.getSegmentId() : result.getDocumentId();
			if (!uniqueResults.containsKey(key) || uniqueResults.get(key).getScore().compareTo(result.getScore()) < 0) {
				uniqueResults.put(key, result);
			}
		}

		return new ArrayList<>(uniqueResults.values());
	}

	/**
	 * 获取召回器配置
	 */
	private PipelineConfig.RetrieverConfig getRetrieverConfig(RetrievalType type, PipelineConfig config) {
		return config.getRetrieverConfigs()
			.stream()
			.filter(c -> c.getType() == type)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("未找到召回器配置: type=" + type));
	}

	/**
	 * 计算类型统计
	 */
	private Map<RetrievalType, Integer> calculateTypeCount(List<RetrievalResult> results) {
		return results.stream()
			.collect(Collectors.groupingBy(RetrievalResult::getRetrievalType,
					Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)));
	}

	/**
	 * todo fxz 配置化 获取默认管道配置
	 */
	private PipelineConfig getDefaultConfig(Long datasetId) {
		PipelineConfig.VectorRetrievalConfig vectorRetrievalConfig = PipelineConfig.VectorRetrievalConfig.builder()
			.embeddingModel(getEmbeddingModel(datasetId))
			.topK(5)
			.build();
		PipelineConfig.GraphRetrievalConfig graphRetrievalConfig = PipelineConfig.GraphRetrievalConfig.builder()
			.maxDepth(2)
			.entityConfidenceThreshold(0.7)
			.includeRelations(Boolean.TRUE)
			.maxNodes(10)
			.build();
		PipelineConfig.QaRetrievalConfig qaRetrievalConfig = PipelineConfig.QaRetrievalConfig.builder()
			.minScore(0.7)
			.topK(3)
			.build();

		return PipelineConfig.builder()
			.enableParallel(true)
			.retrieverConfigs(Arrays.asList(
					PipelineConfig.RetrieverConfig.builder()
						.type(RetrievalType.VECTOR)
						.enabled(true)
						.vectorConfig(vectorRetrievalConfig)
						.build(),
					PipelineConfig.RetrieverConfig.builder()
						.type(RetrievalType.GRAPH)
						.enabled(true)
						.graphConfig(graphRetrievalConfig)
						.build(),
					PipelineConfig.RetrieverConfig.builder()
						.type(RetrievalType.QA)
						.enabled(true)
						.qaConfig(qaRetrievalConfig)
						.build(),
					PipelineConfig.RetrieverConfig.builder()
						.type(RetrievalType.HYBRID)
						.enabled(true)
						.hybridConfig(PipelineConfig.HybridRetrievalConfig.builder()
							.vectorConfig(vectorRetrievalConfig)
							.graphConfig(graphRetrievalConfig)
							.build())
						.build()))
			.fusionConfig(PipelineConfig.FusionConfig.builder().strategy("rrf").rrfK(60).maxResults(20).build())
			.rerankerConfigs(getDefaultRerankerConfigs(datasetId))
			.postProcessConfig(PipelineConfig.PostProcessConfig.builder()
				.deduplicationStrategy("content")
				.finalTopK(10)
				.minScoreFilter(0.6)
				.build())
			.build();
	}

	private List<RerankerConfig> getDefaultRerankerConfigs(Long datasetId) {
		return Arrays.asList(
				RerankerConfig.builder()
					.type(RerankerType.DIVERSITY)
					.enabled(true)
					.topK(20)
					.diversityRerankerConfig(RerankerConfig.DiversityRerankerConfig.builder()
						.embeddingModel(getEmbeddingModel(datasetId))
						.build())
					.build(),
				RerankerConfig.builder()
					.type(RerankerType.LLM_BASED)
					.enabled(true)
					.topK(10)
					.llmRerankerConfig(
							RerankerConfig.LlmRerankerConfig.builder().chatModel(getChatModel(datasetId)).build())
					.build());
	}

	// 这些方法需要根据实际配置实现
	private EmbeddingModel getEmbeddingModel(Long datasetId) {
		AiDatasetsDTO datasetsDTO = aiDatasetsService.findById(datasetId);
		return aiModelRuntimeService.acquireEmbeddingModel(null, Long.valueOf(datasetsDTO.getEmbeddingModel()));
	}

	private EmbeddingStore<TextSegment> getEmbeddingStore(Long datasetId) {
		// todo fxz: 实现
		return aiDocumentsService.getEmbeddingStore(null);
	}

	private ChatModel getChatModel(Long datasetId) {
		AiDatasetsDTO datasetsDTO = aiDatasetsService.findById(datasetId);
		return aiDocumentsService.getChatModel(datasetsDTO.getGraphicModel());
	}

}