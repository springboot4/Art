package com.art.ai.service.dataset.rag.graph.ingest;

import cn.hutool.core.map.MapUtil;
import com.art.ai.service.dataset.rag.constant.KnowledgeConstants;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdge;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeAddInfo;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeEditInfo;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphEntityDesc;
import com.art.ai.service.dataset.rag.graph.entity.GraphEntityRelation;
import com.art.ai.service.dataset.rag.graph.entity.GraphExtraction;
import com.art.ai.service.dataset.rag.graph.entity.GraphExtractionInfo;
import com.art.ai.service.dataset.rag.graph.entity.GraphSearchCondition;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertex;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexUpdateInfo;
import com.art.ai.service.dataset.rag.graph.store.GraphStore;
import com.art.core.common.exception.ArtException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.segment.TextSegmentTransformer;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fxz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GraphStoreIngestor {

	private final GraphStore graphStore;

	private final GraphExtractionService extractionService;

	private final GraphExtractionParser extractionParser;

	public void ingest(List<Document> documents, DocumentSplitter documentSplitter,
			DocumentTransformer documentTransformer, TextSegmentTransformer textSegmentTransformer,
			ChatModel chatModel) {
		log.info("Starting to ingest {} documents into the graph store", documents.size());

		if (documentTransformer != null) {
			documents = documentTransformer.transformAll(documents);
			log.info("Documents were transformed into {} documents", documents.size());
		}

		List<TextSegment> segments = documentSplitter.splitAll(documents);
		log.info("Documents were split into {} segments", segments.size());

		if (textSegmentTransformer != null) {
			segments = textSegmentTransformer.transformAll(segments);
			log.info("Text segments were transformed, now have {} segments", segments.size());
		}

		List<GraphExtraction> extractions = extractionService.extract(chatModel, segments);

		processGraphExtractions(extractions);

		log.info("Finished storing {} text segments into the graph store", segments.size());
	}

	private void processGraphExtractions(List<GraphExtraction> extractions) {
		if (CollectionUtils.isEmpty(extractions)) {
			return;
		}

		for (GraphExtraction graphExtraction : extractions) {
			List<GraphExtractionInfo> extractionInfos = extractionParser.parse(graphExtraction.getResponse());
			if (CollectionUtils.isEmpty(extractionInfos)) {
				log.info("Response is empty or cannot be parsed");
				continue;
			}

			TextSegment segment = graphExtraction.getTextSegment();
			Map<String, Object> metadata = segment.metadata().toMap();

			Filter filter = Optional.ofNullable(buildFilter(metadata))
				.orElseThrow(() -> new ArtException("filter is null"));

			persistExtractionInfos(extractionInfos, filter, String.valueOf(graphExtraction.getSegmentId()), metadata);
		}
	}

	private void persistExtractionInfos(List<GraphExtractionInfo> extractionInfos, Filter filter, String textSegmentId,
			Map<String, Object> metadata) {
		for (GraphExtractionInfo recordAttributes : extractionInfos) {
			Optional.ofNullable(recordAttributes.getEntityDesc())
				.ifPresent(e -> handleEntity(e, filter, textSegmentId, metadata));

			Optional.ofNullable(recordAttributes.getEntityRelation())
				.ifPresent(e -> handleRelation(e, filter, textSegmentId, metadata));
		}
	}

	private void handleEntity(GraphEntityDesc entityDesc, Filter filter, String textSegmentId,
			Map<String, Object> metadata) {
		String entityName = entityDesc.getEntity();
		String entityType = entityDesc.getType();
		String entityDescription = entityDesc.getDescription();
		log.info("entityName:{},entityType:{},entityDescription:{}", entityName, entityType, entityDescription);

		List<GraphVertex> existVertices = graphStore.searchVertices(GraphVertexSearch.builder()
			.label(entityType)
			.limit(1)
			.names(List.of(entityName))
			.metadataFilter(filter)
			.build());

		saveOrUpdateEntityDesc(filter, textSegmentId, metadata, existVertices, entityDescription, entityName,
				entityType);
	}

	private void handleRelation(GraphEntityRelation entityRelation, Filter filter, String textSegmentId,
			Map<String, Object> metadata) {
		String sourceName = entityRelation.getSourceEntity();
		String targetName = entityRelation.getTargetEntity();
		String edgeDescription = entityRelation.getRelation();
		log.info("Relationship sourceName:{},targetName:{},edgeDescription:{}", sourceName, targetName,
				edgeDescription);

		double weight = 1.0;
		if (entityRelation.getWeight() != null) {
			weight = NumberUtils.toDouble(entityRelation.getWeight(), 1.0);
		}

		queryVertexIfAbsentCreate(filter, metadata, sourceName, textSegmentId);
		queryVertexIfAbsentCreate(filter, metadata, targetName, textSegmentId);
		updateOrCreateEdge(filter, metadata, sourceName, targetName, weight, textSegmentId, edgeDescription);
	}

	private void updateOrCreateEdge(Filter filter, Map<String, Object> metadata, String sourceName, String targetName,
			double weight, String chunkId, String edgeDescription) {
		GraphEdgeSearch search = new GraphEdgeSearch();
		search.setSource(GraphSearchCondition.builder().names(List.of(sourceName)).metadataFilter(filter).build());
		search.setTarget(GraphSearchCondition.builder().names(List.of(targetName)).metadataFilter(filter).build());

		Triple<GraphVertex, GraphEdge, GraphVertex> graphEdgeWithVertices = graphStore.getEdge(search);

		if (graphEdgeWithVertices != null) {
			GraphEdge existGraphEdge = graphEdgeWithVertices.getMiddle();
			weight += existGraphEdge.getWeight();
			Map<String, Object> mergedMetadata = mergeMetadata(existGraphEdge.getMetadata(), metadata);

			GraphEdgeEditInfo graphEdgeEditInfo = new GraphEdgeEditInfo();
			graphEdgeEditInfo.setSourceFilter(
					GraphSearchCondition.builder().names(List.of(sourceName)).metadataFilter(filter).build());
			graphEdgeEditInfo.setTargetFilter(
					GraphSearchCondition.builder().names(List.of(targetName)).metadataFilter(filter).build());
			graphEdgeEditInfo.setEdge(GraphEdge.builder()
				.textSegmentId(existGraphEdge.getTextSegmentId() + "," + chunkId)
				.description(existGraphEdge.getDescription() + "\n" + edgeDescription)
				.weight(weight)
				.metadata(mergedMetadata)
				.build());
			graphStore.updateEdge(graphEdgeEditInfo);
		}
		else {
			checkOrCreateVertex("", sourceName, chunkId, filter, metadata);
			checkOrCreateVertex("", targetName, chunkId, filter, metadata);

			GraphEdgeAddInfo addInfo = new GraphEdgeAddInfo();
			addInfo.setEdge(GraphEdge.builder()
				.sourceName(sourceName)
				.targetName(targetName)
				.weight(weight)
				.metadata(metadata)
				.textSegmentId(chunkId)
				.description(edgeDescription)
				.build());
			addInfo.setSourceFilter(
					GraphSearchCondition.builder().names(List.of(sourceName)).metadataFilter(filter).build());
			addInfo.setTargetFilter(
					GraphSearchCondition.builder().names(List.of(targetName)).metadataFilter(filter).build());

			graphStore.addEdge(addInfo);
		}
	}

	private void queryVertexIfAbsentCreate(Filter filter, Map<String, Object> metadata, String sourceName,
			String chunkId) {
		GraphVertex source = graphStore
			.getVertex(GraphVertexSearch.builder().names(List.of(sourceName)).metadataFilter(filter).build());

		Optional.ofNullable(source).ifPresentOrElse((a) -> {
		}, () -> graphStore
			.addVertex(GraphVertex.builder().name(sourceName).textSegmentId(chunkId).metadata(metadata).build()));
	}

	private void saveOrUpdateEntityDesc(Filter filter, String textSegmentId, Map<String, Object> metadata,
			List<GraphVertex> existVertices, String entityDescription, String entityName, String entityType) {
		if (CollectionUtils.isNotEmpty(existVertices)) {
			GraphVertex existVertex = existVertices.get(0);
			String newTextSegmentId = existVertex.getTextSegmentId() + "," + textSegmentId;
			String newDesc = existVertex.getDescription() + "\n" + entityDescription;

			Map<String, Object> mergedMetadata = mergeMetadata(existVertex.getMetadata(), metadata);

			GraphVertex newData = GraphVertex.builder()
				.textSegmentId(newTextSegmentId)
				.description(newDesc)
				.metadata(mergedMetadata)
				.name(entityName)
				.label(entityType)
				.build();
			graphStore.updateVertex(
					GraphVertexUpdateInfo.builder().name(entityName).metadataFilter(filter).newData(newData).build());
		}
		else {
			graphStore.addVertex(GraphVertex.builder()
				.label(entityType)
				.name(entityName)
				.textSegmentId(textSegmentId)
				.description(entityDescription)
				.metadata(metadata)
				.build());
		}
	}

	private Filter buildFilter(Map<String, Object> metadata) {
		return metadata.entrySet()
			.stream()
			.filter(entry -> KnowledgeConstants.DATASET_KEY.equals(entry.getKey()))
			.map(entry -> new IsEqualTo(entry.getKey(), entry.getValue()))
			.findAny()
			.orElse(null);
	}

	private void checkOrCreateVertex(String label, String name, String textSegmentId, Filter metadataFilter,
			Map<String, Object> metadata) {
		List<GraphVertex> existVertices = graphStore.searchVertices(GraphVertexSearch.builder()
			.label(label)
			.limit(1)
			.names(List.of(name))
			.metadataFilter(metadataFilter)
			.build());
		if (CollectionUtils.isEmpty(existVertices)) {
			graphStore.addVertex(GraphVertex.builder()
				.label(label)
				.name(name)
				.textSegmentId(textSegmentId)
				.metadata(metadata)
				.build());
		}
	}

	private Map<String, Object> mergeMetadata(Map<String, Object> existMetadata, Map<String, Object> newMetadata) {
		if (MapUtil.isEmpty(existMetadata)) {
			return new HashMap<>(newMetadata);
		}

		String existDocumentId = (String) existMetadata.getOrDefault(KnowledgeConstants.DOCUMENT_KEY, "");
		String newDocumentId = (String) newMetadata.getOrDefault(KnowledgeConstants.DOCUMENT_KEY, "");

		String mergedDocumentIds = String.join(",",
				Stream.of(existDocumentId.split(","), newDocumentId.split(","))
					.flatMap(Arrays::stream)
					.filter(s -> !s.isBlank())
					.collect(Collectors.toSet()));

		existMetadata.put(KnowledgeConstants.DOCUMENT_KEY, mergedDocumentIds);

		return new HashMap<>(existMetadata);
	}

}
