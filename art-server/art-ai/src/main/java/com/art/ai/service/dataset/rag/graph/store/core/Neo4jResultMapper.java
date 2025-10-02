package com.art.ai.service.dataset.rag.graph.store.core;

import com.art.ai.service.dataset.rag.graph.entity.GraphEdge;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.BooleanValue;
import org.neo4j.driver.internal.value.FloatValue;
import org.neo4j.driver.internal.value.StringValue;
import org.neo4j.driver.types.Entity;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.GRAPH_STORE_MAIN_FIELDS;

/**
 * @author fxz
 */
@Component
public class Neo4jResultMapper {

	public <T> T getFirstOrNull(List<T> list) {
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	public List<GraphVertex> toVertexList(List<Record> records, String vertexName) {
		return records.stream().map(record -> toVertex(record.get(vertexName).asNode())).collect(Collectors.toList());
	}

	public List<Triple<GraphVertex, GraphEdge, GraphVertex>> toEdgeTripleList(List<Record> records, String sourceName,
			String edgeName, String targetName) {
		return records.stream().map(record -> {
			Node sourceNode = record.get(sourceName).asNode();
			Relationship rel = record.get(edgeName).asRelationship();
			Node targetNode = record.get(targetName).asNode();
			return Triple.of(toVertex(sourceNode), toEdge(sourceNode, rel, targetNode), toVertex(targetNode));
		}).collect(Collectors.toList());
	}

	private GraphVertex toVertex(Node node) {
		List<String> labels = new ArrayList<>();
		node.labels().forEach(labels::add);
		return GraphVertex.builder()
			.id(node.elementId())
			.label(String.join(":", labels))
			.name(node.get("name").asString())
			.description(node.get("description").asString())
			.textSegmentId(node.get("text_segment_id").asString())
			.metadata(extractMetadata(node))
			.build();
	}

	private GraphEdge toEdge(Node sourceNode, Relationship rel, Node targetNode) {
		return GraphEdge.builder()
			.id(rel.elementId())
			.startId(sourceNode.elementId())
			.endId(targetNode.elementId())
			.label(rel.type())
			.weight(rel.get("weight").asDouble(0.0))
			.description(rel.get("description").asString())
			.textSegmentId(rel.get("text_segment_id").asString())
			.metadata(extractMetadata(rel))
			.build();
	}

	private Map<String, Object> extractMetadata(Entity entity) {
		return StreamSupport.stream(entity.keys().spliterator(), false)
			.filter(key -> !GRAPH_STORE_MAIN_FIELDS.contains(key))
			.map(key -> {
				Value value = entity.get(key);
				Object mappedValue;

				if (value instanceof StringValue) {
					mappedValue = value.asString();
				}
				else if (value instanceof FloatValue) {
					mappedValue = value.asFloat();
				}
				else if (value instanceof BooleanValue) {
					mappedValue = value.asBoolean();
				}
				else {
					mappedValue = value.isNull() ? null : value.toString();
				}

				return mappedValue == null ? null : Map.entry(key, mappedValue);
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}