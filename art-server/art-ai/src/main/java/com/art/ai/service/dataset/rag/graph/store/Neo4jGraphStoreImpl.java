package com.art.ai.service.dataset.rag.graph.store;

import cn.hutool.core.map.MapUtil;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdge;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeAddInfo;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeEditInfo;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphSearchCondition;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertex;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexUpdateInfo;
import com.art.ai.service.dataset.rag.graph.internal.Neo4jFilterMapper;
import com.art.ai.service.dataset.rag.graph.store.config.Neo4jProperties;
import com.art.ai.service.dataset.rag.graph.store.core.Neo4jCypherQueryBuilder;
import com.art.ai.service.dataset.rag.graph.store.core.Neo4jResultMapper;
import com.art.ai.service.dataset.rag.graph.store.core.Neo4jTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.neo4j.cypherdsl.core.Condition;
import org.neo4j.cypherdsl.core.Cypher;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.cypherdsl.core.Relationship;
import org.neo4j.cypherdsl.core.Statement;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.langchain4j.internal.ValidationUtils.ensureNotEmpty;
import static dev.langchain4j.internal.ValidationUtils.ensureNotNull;

/**
 * @author fxz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Neo4jGraphStoreImpl implements GraphStore {

	private static final String SOURCE_NODE_NAME = "v1";

	private static final String TARGET_NODE_NAME = "v2";

	private static final String EDGE_NAME = "e";

	private static final String VERTEX_NAME = "v";

	private final Neo4jTemplate template;

	private final Neo4jCypherQueryBuilder queryBuilder;

	private final Neo4jResultMapper mapper;

	private final Neo4jProperties properties;

	@Override
	public Triple<GraphVertex, GraphEdge, GraphVertex> addEdge(GraphEdgeAddInfo addInfo) {
		ensureNotNull(addInfo.getEdge(), "Graph edge");

		Node sourceNode = queryBuilder.createNode(SOURCE_NODE_NAME);
		Node targetNode = queryBuilder.createNode(TARGET_NODE_NAME);
		Relationship newEdge = sourceNode.relationshipTo(targetNode, properties.getDatabase()).named(EDGE_NAME);

		Condition condition = queryBuilder.buildVertexCondition(addInfo.getSourceFilter(), sourceNode)
			.and(queryBuilder.buildVertexCondition(addInfo.getTargetFilter(), targetNode));

		Statement statement = queryBuilder.buildAddEdgeStatement(sourceNode, targetNode, newEdge, condition);

		Map<String, Object> params = Map.of("props", buildEdgeProperties(addInfo.getEdge()));
		List<Record> records = template.executeWrite(statement, params);
		return mapper.getFirstOrNull(mapper.toEdgeTripleList(records, SOURCE_NODE_NAME, EDGE_NAME, TARGET_NODE_NAME));
	}

	@Override
	public Triple<GraphVertex, GraphEdge, GraphVertex> updateEdge(GraphEdgeEditInfo edgeEditInfo) {
		ensureNotNull(edgeEditInfo.getEdge(), "Graph edit info");

		Node sourceNode = queryBuilder.createNode(SOURCE_NODE_NAME);
		Node targetNode = queryBuilder.createNode(TARGET_NODE_NAME);
		Relationship edge = sourceNode.relationshipBetween(targetNode).named(EDGE_NAME);

		Condition condition = queryBuilder.buildVertexCondition(edgeEditInfo.getSourceFilter(), sourceNode)
			.and(queryBuilder.buildVertexCondition(edgeEditInfo.getTargetFilter(), targetNode));

		Statement statement = queryBuilder.buildUpdateEdgeStatement(sourceNode, targetNode, edge, condition);

		Map<String, Object> params = Map.of("props", buildEdgeProperties(edgeEditInfo.getEdge()));
		List<Record> records = template.executeWrite(statement, params);
		return mapper.getFirstOrNull(mapper.toEdgeTripleList(records, SOURCE_NODE_NAME, EDGE_NAME, TARGET_NODE_NAME));
	}

	@Override
	public Triple<GraphVertex, GraphEdge, GraphVertex> getEdge(GraphEdgeSearch search) {
		return mapper.getFirstOrNull(searchEdges(search));
	}

	@Override
	public List<Triple<GraphVertex, GraphEdge, GraphVertex>> searchEdges(GraphEdgeSearch search) {
		Node sourceNode = queryBuilder.createNode(SOURCE_NODE_NAME);
		Node targetNode = queryBuilder.createNode(TARGET_NODE_NAME);
		Relationship edge = sourceNode.relationshipBetween(targetNode).named(EDGE_NAME);

		Node dummyEdgeNode = queryBuilder.createNode(EDGE_NAME);

		Condition sourceCondition = queryBuilder.buildVertexCondition(search.getSource(), sourceNode);
		Condition targetCondition = queryBuilder.buildVertexCondition(search.getTarget(), targetNode);
		Condition edgeCondition = (search.getEdge() == null) ? Cypher.noCondition()
				: new Neo4jFilterMapper(dummyEdgeNode).getCondition(search.getEdge().getMetadataFilter());

		Condition finalCondition = Stream.of(sourceCondition, targetCondition, edgeCondition)
			.filter(c -> c != Cypher.noCondition())
			.reduce(Condition::and)
			.orElseGet(Cypher::noCondition);

		Statement statement = queryBuilder.buildSearchEdgesStatement(sourceNode, targetNode, edge, finalCondition,
				search.getLimit());

		List<Record> records = template.executeRead(statement, Collections.emptyMap());
		return mapper.toEdgeTripleList(records, SOURCE_NODE_NAME, EDGE_NAME, TARGET_NODE_NAME);
	}

	@Override
	public boolean addVertex(GraphVertex vertex) {
		ensureNotNull(vertex, "Vertex");

		return addVertexes(List.of(vertex));
	}

	@Override
	public boolean addVertexes(List<GraphVertex> vertexes) {
		ensureNotEmpty(vertexes, "Vertex list cannot be empty.");

		template.executeBatchWrite(tx -> {
			vertexes.stream()
				.collect(Collectors.groupingBy(v -> StringUtils.defaultString(v.getLabel())))
				.forEach((label, verticesInGroup) -> {
					String dynamicLabel = StringUtils.isNotBlank(label) ? ":" + label : "";
					String cypher = String.format("UNWIND $props AS properties CREATE (n:%s%s) SET n = properties",
							properties.getDatabase(), dynamicLabel);
					log.info("Executing batch addVertexes cypher: {}", cypher);
					List<Map<String, Object>> propsList = verticesInGroup.stream()
						.map(this::buildVertexProperties)
						.toList();
					tx.run(cypher, Map.of("props", propsList));
				});
			return null;
		});

		return true;
	}

	@Override
	public void deleteVertices(GraphSearchCondition filter) {
		Node node = queryBuilder.createNode(VERTEX_NAME);
		Condition condition = queryBuilder.buildVertexCondition(filter, node);
		Statement statement = queryBuilder.buildDeleteVerticesStatement(node, condition);

		template.executeWrite(statement, Collections.emptyMap());
	}

	@Override
	public GraphVertex getVertex(GraphVertexSearch search) {
		return mapper.getFirstOrNull(searchVertices(search));
	}

	@Override
	public GraphVertex updateVertex(GraphVertexUpdateInfo updateInfo) {
		ensureNotNull(updateInfo.getNewData(), "New data for vertex update");

		Node node = queryBuilder.createNode(VERTEX_NAME);
		Condition condition = queryBuilder.buildUpdateVertexCondition(updateInfo, node);
		Statement statement = queryBuilder.buildUpdateVertexStatement(node, condition);

		Map<String, Object> params = Map.of("props", buildVertexProperties(updateInfo.getNewData()));
		List<Record> records = template.executeWrite(statement, params);
		return mapper.getFirstOrNull(mapper.toVertexList(records, VERTEX_NAME));
	}

	@Override
	public List<GraphVertex> searchVertices(GraphVertexSearch search) {
		Node node = queryBuilder.createNode(VERTEX_NAME, search.getLabel());
		Condition condition = queryBuilder.buildVertexCondition(search, node);
		Statement statement = queryBuilder.buildSearchVerticesStatement(node, condition, search.getLimit());

		List<Record> records = template.executeRead(statement, Collections.emptyMap());
		return mapper.toVertexList(records, VERTEX_NAME);
	}

	private Map<String, Object> buildVertexProperties(GraphVertex vertex) {
		Map<String, Object> props = new HashMap<>(vertex.getMetadata());
		props.put("name", vertex.getName());
		props.put("text_segment_id", StringUtils.defaultString(vertex.getTextSegmentId()));
		props.put("description", vertex.getDescription());
		return props;
	}

	private Map<String, Object> buildEdgeProperties(GraphEdge edge) {
		Map<String, Object> props = new HashMap<>(MapUtil.emptyIfNull(edge.getMetadata()));
		props.put("text_segment_id", edge.getTextSegmentId());
		props.put("description", edge.getDescription());
		props.put("weight", edge.getWeight());
		return props;
	}

}
