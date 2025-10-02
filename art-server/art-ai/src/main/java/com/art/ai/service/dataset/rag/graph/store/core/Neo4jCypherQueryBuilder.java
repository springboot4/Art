package com.art.ai.service.dataset.rag.graph.store.core;

import com.art.ai.service.dataset.rag.graph.entity.GraphSearchCondition;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexUpdateInfo;
import com.art.ai.service.dataset.rag.graph.internal.Neo4jFilterMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.cypherdsl.core.Condition;
import org.neo4j.cypherdsl.core.Cypher;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.cypherdsl.core.Relationship;
import org.neo4j.cypherdsl.core.Statement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.neo4j.cypherdsl.core.Cypher.match;
import static org.neo4j.cypherdsl.core.Cypher.node;

/**
 * @author fxz
 */
@Component
public class Neo4jCypherQueryBuilder {

	private final String graphName;

	public Neo4jCypherQueryBuilder(@Value("${neo4j.database:neo4j}") String graphName) {
		this.graphName = graphName;
	}

	public Statement buildAddEdgeStatement(Node sourceNode, Node targetNode, Relationship newEdge,
			Condition condition) {
		return match(sourceNode, targetNode).where(condition)
			.create(newEdge)
			.set(newEdge, Cypher.parameter("props"))
			.returning(sourceNode, targetNode, newEdge)
			.build();
	}

	public Statement buildUpdateEdgeStatement(Node sourceNode, Node targetNode, Relationship edge,
			Condition condition) {
		return match(sourceNode, edge, targetNode).where(condition)
			.set(edge, Cypher.parameter("props"))
			.returning(sourceNode, targetNode, edge)
			.build();
	}

	public Statement buildSearchEdgesStatement(Node sourceNode, Node targetNode, Relationship edge, Condition condition,
			int limit) {
		return match(sourceNode, edge, targetNode).where(condition)
			.returning(sourceNode, targetNode, edge)
			.orderBy(Cypher.raw("elementId(e)").descending())
			.limit(limit)
			.build();
	}

	public Statement buildUpdateVertexStatement(Node node, Condition condition) {
		return match(node).where(condition).set(node, Cypher.parameter("props")).returning(node).limit(1).build();
	}

	public Statement buildSearchVerticesStatement(Node node, Condition condition, int limit) {
		return match(node).where(condition)
			.returning(node)
			.orderBy(node.property("name").descending())
			.limit(limit)
			.build();
	}

	public Condition buildVertexCondition(GraphSearchCondition filter, Node node) {
		if (filter == null) {
			return Cypher.noCondition();
		}
		Condition metaCondition = new Neo4jFilterMapper(node).getCondition(filter.getMetadataFilter());
		return CollectionUtils.isNotEmpty(filter.getNames())
				? node.property("name").in(Cypher.literalOf(filter.getNames())).and(metaCondition) : metaCondition;
	}

	public Condition buildUpdateVertexCondition(GraphVertexUpdateInfo updateInfo, Node node) {
		return node.property("name")
			.eq(Cypher.literalOf(updateInfo.getName()))
			.and(new Neo4jFilterMapper(node).getCondition(updateInfo.getMetadataFilter()));
	}

	public Node createNode(String name) {
		return node(graphName).named(name);
	}

	public Node createNode(String name, String label) {
		return StringUtils.isNotBlank(label) ? node(graphName, label).named(name) : createNode(name);
	}

	public Statement buildDeleteVerticesStatement(Node node, Condition condition) {
		return match(node).where(condition).detachDelete(node).build();
	}

}