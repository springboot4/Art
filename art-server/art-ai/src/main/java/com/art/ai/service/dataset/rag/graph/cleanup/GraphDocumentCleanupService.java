package com.art.ai.service.dataset.rag.graph.cleanup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DOCUMENT_KEY;

/**
 * 知识图谱文档清理服务 负责智能删除文档相关的实体和关系，考虑多文档共享的情况 直接使用Neo4j Driver操作，不依赖GraphStore
 *
 * @author fxz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GraphDocumentCleanupService {

	private final Driver neo4jDriver;

	/**
	 * 智能删除文档相关的图谱数据
	 * @param documentId 要删除的文档ID
	 */
	@Transactional
	public void cleanupDocumentGraphData(Long documentId) {
		log.info("开始清理文档 {} 的图谱数据", documentId);

		// 1. 处理实体清理
		cleanupDocumentVertices(documentId);

		// 2. 处理关系清理
		cleanupDocumentEdges(documentId);

		log.info("完成文档 {} 的图谱数据清理", documentId);
	}

	/**
	 * 清理文档相关的实体
	 */
	private void cleanupDocumentVertices(Long documentId) {
		log.info("开始清理文档 {} 的实体", documentId);

		// 查找包含该文档ID的所有实体
		String cypher = """
				MATCH (n)
				WHERE n.%s CONTAINS $documentId
				RETURN n
				""".formatted(DOCUMENT_KEY);

		List<Record> records;
		try (Session session = neo4jDriver.session()) {
			records = session.run(cypher, Map.of("documentId", String.valueOf(documentId))).list();
		}

		if (records.isEmpty()) {
			log.info("文档 {} 没有关联的实体", documentId);
			return;
		}

		log.info("找到 {} 个与文档 {} 相关的实体", records.size(), documentId);

		for (Record record : records) {
			Value value = record.get("n");
			Node n = value.asNode();
			Map<String, Object> node = n.asMap();
			processVertexCleanup(node, documentId);
		}
	}

	/**
	 * 处理单个实体的清理
	 */
	private void processVertexCleanup(Map<String, Object> node, Long documentId) {
		String nodeName = (String) node.get("name");
		String documentIdsStr = (String) node.get(DOCUMENT_KEY);

		if (StringUtils.isBlank(documentIdsStr)) {
			log.warn("实体 {} 没有文档元数据，直接删除", nodeName);
			deleteVertex(node);
			return;
		}

		// 解析文档ID列表
		Set<String> documentIds = Arrays.stream(documentIdsStr.split(","))
			.map(String::trim)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toSet());

		String documentIdStr = String.valueOf(documentId);

		if (!documentIds.contains(documentIdStr)) {
			log.warn("实体 {} 的元数据中不包含文档 {}，跳过处理", nodeName, documentId);
			return;
		}

		// 如果该实体只属于这一个文档，直接删除
		if (documentIds.size() == 1) {
			log.info("实体 {} 只属于文档 {}，直接删除", nodeName, documentId);
			deleteVertex(node);
		}
		else {
			// 如果该实体还属于其他文档，只从元数据中移除该文档ID
			log.info("实体 {} 还属于其他文档，从元数据中移除文档 {}", nodeName, documentId);
			updateVertexMetadata(node, documentId);
		}
	}

	/**
	 * 删除实体
	 */
	private void deleteVertex(Map<String, Object> node) {
		String nodeName = (String) node.get("name");
		String documentIdsStr = (String) node.get(DOCUMENT_KEY);

		String cypher = """
				MATCH (n {name: $name, %s: $document})
				DETACH DELETE n
				""".formatted(DOCUMENT_KEY);

		try (Session session = neo4jDriver.session()) {
			session.run(cypher, Map.of("name", nodeName, "document", documentIdsStr));
		}

		log.info("已删除实体: {}", nodeName);
	}

	/**
	 * 更新实体元数据，移除指定的文档ID
	 */
	private void updateVertexMetadata(Map<String, Object> node, Long documentId) {
		String nodeName = (String) node.get("name");
		String documentIdsStr = (String) node.get(DOCUMENT_KEY);

		// 移除指定的文档ID
		Set<String> documentIds = Arrays.stream(documentIdsStr.split(","))
			.map(String::trim)
			.filter(StringUtils::isNotBlank)
			.filter(id -> !id.equals(String.valueOf(documentId)))
			.collect(Collectors.toSet());

		if (documentIds.isEmpty()) {
			log.warn("移除文档 {} 后，实体 {} 没有其他文档关联，直接删除", documentId, nodeName);
			deleteVertex(node);
			return;
		}

		// 更新元数据
		String newDocumentIdsStr = String.join(",", documentIds);

		String cypher = """
				MATCH (n {name: $name, %s: $oldDocument})
				SET n.%s = $newDocument
				""".formatted(DOCUMENT_KEY, DOCUMENT_KEY);

		try (Session session = neo4jDriver.session()) {
			session.run(cypher,
					Map.of("name", nodeName, "oldDocument", documentIdsStr, "newDocument", newDocumentIdsStr));
		}

		log.info("已更新实体 {} 的元数据，移除文档 {}", nodeName, documentId);
	}

	/**
	 * 清理文档相关的关系
	 */
	private void cleanupDocumentEdges(Long documentId) {
		log.info("开始清理文档 {} 的关系", documentId);

		// 查找包含该文档ID的所有关系
		String cypher = """
				MATCH (a)-[r]->(b)
				WHERE r.%s CONTAINS $documentId
				RETURN a, r, b
				""".formatted(DOCUMENT_KEY);

		List<Record> records;
		try (Session session = neo4jDriver.session()) {
			records = session.run(cypher, Map.of("documentId", String.valueOf(documentId))).list();
		}

		if (records.isEmpty()) {
			log.info("文档 {} 没有关联的关系", documentId);
			return;
		}

		log.info("找到 {} 个与文档 {} 相关的关系", records.size(), documentId);

		for (Record result : records) {
			Map<String, Object> sourceNode = result.get("a").asNode().asMap();
			Map<String, Object> relationship = result.get("r").asRelationship().asMap();
			Map<String, Object> targetNode = result.get("b").asNode().asMap();
			processEdgeCleanup(sourceNode, relationship, targetNode, documentId);
		}
	}

	/**
	 * 处理单个关系的清理
	 */
	private void processEdgeCleanup(Map<String, Object> sourceNode, Map<String, Object> relationship,
			Map<String, Object> targetNode, Long documentId) {
		String sourceName = (String) sourceNode.get("name");
		String targetName = (String) targetNode.get("name");
		String documentIdsStr = (String) relationship.get(DOCUMENT_KEY);

		if (StringUtils.isBlank(documentIdsStr)) {
			log.warn("关系 {} -> {} 没有文档元数据，直接删除", sourceName, targetName);
			deleteEdge(sourceNode, relationship, targetNode);
			return;
		}

		// 解析文档ID列表
		Set<String> documentIds = Arrays.stream(documentIdsStr.split(","))
			.map(String::trim)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toSet());

		String documentIdStr = String.valueOf(documentId);

		if (!documentIds.contains(documentIdStr)) {
			log.warn("关系 {} -> {} 的元数据中不包含文档 {}，跳过处理", sourceName, targetName, documentId);
			return;
		}

		// 如果该关系只属于这一个文档，直接删除
		if (documentIds.size() == 1) {
			log.info("关系 {} -> {} 只属于文档 {}，直接删除", sourceName, targetName, documentId);
			deleteEdge(sourceNode, relationship, targetNode);
		}
		else {
			// 如果该关系还属于其他文档，只从元数据中移除该文档ID
			log.info("关系 {} -> {} 还属于其他文档，从元数据中移除文档 {}", sourceName, targetName, documentId);
			updateEdgeMetadata(sourceNode, relationship, targetNode, documentId);
		}
	}

	/**
	 * 删除关系
	 */
	private void deleteEdge(Map<String, Object> sourceNode, Map<String, Object> relationship,
			Map<String, Object> targetNode) {
		String sourceName = (String) sourceNode.get("name");
		String targetName = (String) targetNode.get("name");
		String documentIdsStr = (String) relationship.get(DOCUMENT_KEY);

		String cypher = """
				MATCH (a {name: $sourceName})-[r]->(b {name: $targetName})
				WHERE r.%s = $document
				DELETE r
				""".formatted(DOCUMENT_KEY);

		try (Session session = neo4jDriver.session()) {
			session.run(cypher, Map.of("sourceName", sourceName, "targetName", targetName, "document", documentIdsStr));
		}

		log.info("已删除关系: {} -> {}", sourceName, targetName);
	}

	/**
	 * 更新关系元数据，移除指定的文档ID
	 */
	private void updateEdgeMetadata(Map<String, Object> sourceNode, Map<String, Object> relationship,
			Map<String, Object> targetNode, Long documentId) {
		String sourceName = (String) sourceNode.get("name");
		String targetName = (String) targetNode.get("name");
		String documentIdsStr = (String) relationship.get(DOCUMENT_KEY);

		// 移除指定的文档ID
		Set<String> documentIds = Arrays.stream(documentIdsStr.split(","))
			.map(String::trim)
			.filter(StringUtils::isNotBlank)
			.filter(id -> !id.equals(String.valueOf(documentId)))
			.collect(Collectors.toSet());

		if (documentIds.isEmpty()) {
			log.warn("移除文档 {} 后，关系 {} -> {} 没有其他文档关联，直接删除", documentId, sourceName, targetName);
			deleteEdge(sourceNode, relationship, targetNode);
			return;
		}

		// 更新元数据
		String newDocumentIdsStr = String.join(",", documentIds);

		String cypher = """
				MATCH (a {name: $sourceName})-[r]->(b {name: $targetName})
				WHERE r.%s = $oldDocument
				SET r.%s = $newDocument
				""".formatted(DOCUMENT_KEY, DOCUMENT_KEY);

		try (Session session = neo4jDriver.session()) {
			session.run(cypher, Map.of("sourceName", sourceName, "targetName", targetName, "oldDocument",
					documentIdsStr, "newDocument", newDocumentIdsStr));
		}

		log.info("已更新关系 {} -> {} 的元数据，移除文档 {}", sourceName, targetName, documentId);
	}

}