package com.art.ai.service.dataset.rag.embedding.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;

/**
 * @author fxz
 */
@AllArgsConstructor
@Builder
@Getter
public class EmbeddingModelConfig {

	private String apiKey;

	private String baseUrl;

	private int maxRetries;

	private Duration timeout;

	private int dimensions;

	private String modelName;

}
