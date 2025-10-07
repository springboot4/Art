package com.art.ai.service.dataset.rag.retrieval.service;

import com.art.ai.service.dataset.rag.graph.entity.GraphEdge;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphSearchCondition;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertex;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexSearch;
import com.art.ai.service.dataset.rag.graph.store.GraphStore;
import com.art.ai.service.dataset.rag.ner.EntityRecognitionService;
import com.art.ai.service.dataset.rag.ner.RecognizedEntity;
import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.core.common.util.AsyncUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.embedding.filter.comparison.ContainsString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DATASET_KEY;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DOCUMENT_KEY;

/**
 * 基于实体识别的图谱召回服务
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GraphRetrievalService {

	private final GraphStore graphStore;

	private final EntityRecognitionService entityRecognitionService;

	/**
	 * 基于实体识别的图谱召回
	 */
	public List<RetrievalResult> retrieve(String query, Long datasetId, ChatModel chatModel,
			PipelineConfig.GraphRetrievalConfig config) {
		try {
			// 1. 从查询中识别实体
			List<RecognizedEntity> entities = entityRecognitionService.getHighConfidenceEntities(query, chatModel,
					config.getEntityConfidenceThreshold() != null ? config.getEntityConfidenceThreshold() : 0.7);

			if (entities.isEmpty()) {
				log.warn("未从查询中识别出高置信度实体: {}", query);
				return Collections.emptyList();
			}

			// 2. 基于识别的实体进行图谱查询
			List<RetrievalResult> allResults = new ArrayList<>();

			for (RecognizedEntity entity : entities) {
				List<RetrievalResult> entityResults = searchByEntity(entity, datasetId, config, query);
				allResults.addAll(entityResults);
			}

			// 3. 如果启用关系扩展，进行关系遍历
			if (config.getIncludeRelations() != null && config.getIncludeRelations()) {
				allResults.addAll(expandWithRelations(entities, datasetId, config, query));
			}

			return allResults;
		}
		catch (Exception e) {
			log.error("图谱召回失败: query={}, datasetId={}", query, datasetId, e);
			throw new RuntimeException("图谱召回失败", e);
		}
	}

	/**
	 * 基于实体搜索图谱节点
	 */
	private List<RetrievalResult> searchByEntity(RecognizedEntity entity, Long datasetId,
			PipelineConfig.GraphRetrievalConfig config, String originalQuery) {
		GraphVertexSearch search = new GraphVertexSearch();
		search.setNames(List.of(entity.getName().toUpperCase()));
		search.setLabel(entity.getType().getCode().toUpperCase());
		search.setMetadataFilter(new ContainsString(DATASET_KEY, datasetId.toString()));
		int maxNodes = config.getMaxNodes() != null ? config.getMaxNodes() : 10;
		search.setLimit(maxNodes);

		// 执行搜索
		List<GraphVertex> vertices = graphStore.searchVertices(search);

		// 转换为召回结果
		return vertices.stream().map(this::convertToRetrievalResult).collect(Collectors.toList());
	}

	/**
	 * 通过关系扩展搜索
	 */
	private List<RetrievalResult> expandWithRelations(List<RecognizedEntity> entities, Long datasetId,
			PipelineConfig.GraphRetrievalConfig config, String originalQuery) {
		List<RetrievalResult> expandedResults = new ArrayList<>();
		int maxDepth = config.getMaxDepth() != null ? config.getMaxDepth() : 2;

		for (RecognizedEntity entity : entities) {
			// 多跳关系遍历
			Set<String> visitedNodes = new HashSet<>();
			expandedResults.addAll(expandFromEntity(entity.getName().toUpperCase(), datasetId, maxDepth, 0,
					visitedNodes, originalQuery));
		}

		return expandedResults;
	}

	/**
	 * 从特定实体开始的关系扩展
	 */
	private List<RetrievalResult> expandFromEntity(String entityName, Long datasetId, int maxDepth, int currentDepth,
			Set<String> visitedNodes, String originalQuery) {
		if (currentDepth >= maxDepth || visitedNodes.contains(entityName)) {
			return Collections.emptyList();
		}

		visitedNodes.add(entityName);
		List<RetrievalResult> results = new ArrayList<>();

		AsyncUtil.parallel(() -> {
			GraphSearchCondition graphSearchCondition = new GraphSearchCondition();
			graphSearchCondition.setNames(List.of(entityName));
			graphSearchCondition.setMetadataFilter(new ContainsString(DATASET_KEY, datasetId.toString()));
			GraphEdgeSearch search = new GraphEdgeSearch();
			search.setSource(graphSearchCondition);
			List<Triple<GraphVertex, GraphEdge, GraphVertex>> relatedVertices = graphStore.searchEdges(search);
			dfsFromRelated(datasetId, maxDepth, currentDepth, visitedNodes, originalQuery, results, relatedVertices);
		}, () -> {
			GraphEdgeSearch search = new GraphEdgeSearch();
			GraphSearchCondition graphSearchCondition = new GraphSearchCondition();
			graphSearchCondition.setNames(List.of(entityName));
			graphSearchCondition.setMetadataFilter(new ContainsString(DATASET_KEY, datasetId.toString()));
			search.setTarget(graphSearchCondition);
			List<Triple<GraphVertex, GraphEdge, GraphVertex>> relatedVertices = graphStore.searchEdges(search);
			dfsFromRelated(datasetId, maxDepth, currentDepth, visitedNodes, originalQuery, results, relatedVertices);
		});

		return results;
	}

	private void dfsFromRelated(Long datasetId, int maxDepth, int currentDepth, Set<String> visitedNodes,
			String originalQuery, List<RetrievalResult> results,
			List<Triple<GraphVertex, GraphEdge, GraphVertex>> relatedVertices) {
		for (Triple<GraphVertex, GraphEdge, GraphVertex> triple : relatedVertices) {
			GraphVertex vertex = triple.getRight();
			GraphEdge edge = triple.getMiddle();
			results.add(convertToRetrievalResult(vertex));
			results.add(convertToRetrievalResult(edge));

			// 递归扩展
			if (currentDepth < maxDepth - 1) {
				results.addAll(expandFromEntity(vertex.getName(), datasetId, maxDepth, currentDepth + 1, visitedNodes,
						originalQuery));
			}
		}
	}

	/**
	 * 转换为召回结果（带实体信息）
	 */
	private RetrievalResult convertToRetrievalResult(GraphVertex vertex) {
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("vertexId", vertex.getId());
		metadata.put("label", vertex.getLabel());
		metadata.put("description", vertex.getDescription());

		Map<String, Object> vertexMetadata = vertex.getMetadata();
		return RetrievalResult.builder()
			.documentId(String.valueOf(vertexMetadata.get(DOCUMENT_KEY)))
			.segmentId(String.valueOf(vertex.getTextSegmentId()))
			.content(buildContentText(vertex))
			.score(BigDecimal.valueOf(1.0))
			.retrievalType(RetrievalType.GRAPH)
			.metadata(metadata)
			.build();
	}

	/**
	 * 转换为召回结果（带实体信息）
	 */
	private RetrievalResult convertToRetrievalResult(GraphEdge edge) {
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("vertexId", edge.getId());
		metadata.put("label", edge.getLabel());
		metadata.put("description", edge.getDescription());

		Map<String, Object> vertexMetadata = edge.getMetadata();
		return RetrievalResult.builder()
			.documentId(String.valueOf(vertexMetadata.get(DOCUMENT_KEY)))
			.segmentId(String.valueOf(edge.getTextSegmentId()))
			.content(edge.getDescription())
			.score(BigDecimal.valueOf(1.0))
			.retrievalType(RetrievalType.GRAPH)
			.metadata(metadata)
			.build();
	}

	/**
	 * 构建内容文本
	 */
	private String buildContentText(GraphVertex vertex) {
		StringBuilder content = new StringBuilder();
		content.append("实体: ").append(vertex.getName());
		if (vertex.getLabel() != null) {
			content.append(" (").append(vertex.getLabel()).append(")");
		}
		if (vertex.getDescription() != null) {
			content.append("\n描述: ").append(vertex.getDescription());
		}
		return content.toString();
	}

}