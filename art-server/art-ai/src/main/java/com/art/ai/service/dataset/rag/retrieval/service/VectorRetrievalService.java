package com.art.ai.service.dataset.rag.retrieval.service;

import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.ai.service.dataset.service.AiDocumentSegmentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DATASET_KEY;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.SEGMENT_KEY;

/**
 * 向量召回服务
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VectorRetrievalService {

	private final AiDocumentSegmentService aiDocumentSegmentService;

	/**
	 * 向量召回
	 */
	public List<RetrievalResult> retrieve(String query, Long datasetId, EmbeddingModel embeddingModel,
			EmbeddingStore<TextSegment> embeddingStore, PipelineConfig.VectorRetrievalConfig vectorConfig) {
		try {
			// 1. 对查询进行向量化
			Embedding queryEmbedding = embeddingModel.embed(query).content();

			// 2. 设置搜索参数
			int maxResults = vectorConfig.getTopK() != null ? vectorConfig.getTopK() : 10;
			double minScore = vectorConfig.getMinScore() != null ? vectorConfig.getMinScore() : 0.3;

			// 3. 执行向量搜索
			EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
				.queryEmbedding(queryEmbedding)
				.minScore(minScore)
				.maxResults(maxResults)
				.filter(new IsEqualTo(DATASET_KEY, datasetId))
				.build();
			List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(searchRequest).matches();

			// 4. 转换为召回结果
			return matches.stream()
				.map(match -> convertToRetrievalResult(match, datasetId))
				.collect(Collectors.toList());
		}
		catch (Exception e) {
			log.error("向量召回失败: query={}, datasetId={}", query, datasetId, e);
			throw new RuntimeException("向量召回失败", e);
		}
	}

	/**
	 * 转换为召回结果
	 */
	private RetrievalResult convertToRetrievalResult(EmbeddingMatch<TextSegment> match, Long datasetId) {
		TextSegment segment = match.embedded();
		String segmentId = segment.metadata().getString(SEGMENT_KEY);

		// 获取文档段落详细信息
		AiDocumentSegmentDTO segmentDTO = null;
		if (segmentId != null) {
			segmentDTO = aiDocumentSegmentService.findById(Long.valueOf(segmentId));
		}

		Map<String, Object> metadata = new HashMap<>();
		metadata.put("embeddingScore", match.score());
		metadata.put("datasetId", datasetId);
		if (segmentDTO != null) {
			metadata.put("documentId", segmentDTO.getDocumentId());
			metadata.put("segmentId", segmentDTO.getId());
		}

		return RetrievalResult.builder()
			.documentId(segmentDTO != null ? segmentDTO.getDocumentId().toString() : null)
			.segmentId(segmentId)
			.content(segment.text())
			.score(BigDecimal.valueOf(match.score()))
			.retrievalType(RetrievalType.VECTOR)
			.metadata(metadata)
			.build();
	}

}