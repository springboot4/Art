package com.art.ai.service.dataset.rag.graph.store.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fxz
 */
@Data
@Component
@ConfigurationProperties(prefix = "tmp.graph.neo4j")
public class Neo4jProperties {

	private String host;

	private String port;

	private String user;

	private String password;

	private String database = "neo4j";

}
