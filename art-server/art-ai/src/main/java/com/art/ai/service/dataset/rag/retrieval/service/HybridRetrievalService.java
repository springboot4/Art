package com.art.ai.service.dataset.rag.retrieval.service;

import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.core.common.util.AsyncUtil;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 混合召回服务 - 简化版，不再包含融合逻辑 职责：协调多个召回器执行，返回原始结果 融合逻辑统一由 RetrievalPipeline 处理
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HybridRetrievalService {

	private final VectorRetrievalService vectorRetrievalService;

	private final GraphRetrievalService graphRetrievalService;

	/**
	 * 混合召回 - 返回多个召回器的原始结果 不进行融合，融合由上层Pipeline负责
	 */
	private Map<RetrievalType, List<RetrievalResult>> retrieveAll(String query, Long datasetId,
			EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, ChatModel chatModel,
			PipelineConfig.RetrieverConfig retrieverConfig) {
		Map<RetrievalType, List<RetrievalResult>> results = new HashMap<>();

		try {
			// 并行执行
			AsyncUtil.parallel(() -> {
				List<RetrievalResult> vectorResults = vectorRetrievalService.retrieve(query, datasetId, embeddingModel,
						embeddingStore, retrieverConfig.getHybridConfig().getVectorConfig());
				results.put(RetrievalType.VECTOR, vectorResults);
			}, () -> {
				List<RetrievalResult> graphResults = graphRetrievalService.retrieve(query, datasetId, chatModel,
						retrieverConfig.getHybridConfig().getGraphConfig());
				results.put(RetrievalType.GRAPH, graphResults);
			});
		}
		catch (Exception e) {
			log.error("混合召回失败: query={}, datasetId={}", query, datasetId, e);
			throw new RuntimeException("混合召回失败", e);
		}

		return results;
	}

	/**
	 * 混合检索
	 */
	public List<RetrievalResult> retrieve(String query, Long datasetId, EmbeddingModel embeddingModel,
			EmbeddingStore<TextSegment> embeddingStore, ChatModel chatModel,
			PipelineConfig.RetrieverConfig retrieverConfig) {
		Map<RetrievalType, List<RetrievalResult>> allResults = retrieveAll(query, datasetId, embeddingModel,
				embeddingStore, chatModel, retrieverConfig);

		List<RetrievalResult> mergedResults = new ArrayList<>();
		for (List<RetrievalResult> typeResults : allResults.values()) {
			mergedResults.addAll(typeResults);
		}

		return mergedResults;
	}

}