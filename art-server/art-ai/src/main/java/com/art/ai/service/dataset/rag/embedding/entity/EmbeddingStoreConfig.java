package com.art.ai.service.dataset.rag.embedding.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author fxz
 * @since 2025/9/15 13:17
 */
@AllArgsConstructor
@Builder
@Getter
public class EmbeddingStoreConfig {

	private String storeType;

	private Long collectionId;

}
