package com.art.ai.service.dataset.rag.graph.store.config;

import lombok.RequiredArgsConstructor;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fxz
 */
@Configuration
@RequiredArgsConstructor
public class Neo4jConfiguration {

	private final Neo4jProperties neo4jProperties;

	@Bean(destroyMethod = "close")
	public Driver neo4jDriver() {
		String uri = String.format("neo4j://%s:%s", neo4jProperties.getHost(), neo4jProperties.getPort());
		return GraphDatabase.driver(uri, AuthTokens.basic(neo4jProperties.getUser(), neo4jProperties.getPassword()));
	}

}
