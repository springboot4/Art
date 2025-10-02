package com.art.ai.service.dataset.rag.graph.store.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.cypherdsl.core.Statement;
import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author fxz
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Neo4jTemplate {

	private static final Renderer CYPHER_RENDERER = Renderer.getDefaultRenderer();

	private final Driver driver;

	public List<Record> executeRead(Statement statement, Map<String, Object> params) {
		String cypher = CYPHER_RENDERER.render(statement);
		log.info("Executing Cypher [READ]: {}", cypher);
		try (Session session = driver.session()) {
			return session.executeRead(tx -> tx.run(cypher, params).list());
		}
	}

	public List<Record> executeWrite(Statement statement, Map<String, Object> params) {
		String cypher = CYPHER_RENDERER.render(statement);
		log.info("Executing Cypher [WRITE]: {}", cypher);
		try (Session session = driver.session()) {
			return session.executeWrite(tx -> tx.run(cypher, params).list());
		}
	}

	public void executeBatchWrite(TransactionCallback<Void> callback) {
		try (Session session = driver.session()) {
			session.executeWrite(callback);
		}
	}

}