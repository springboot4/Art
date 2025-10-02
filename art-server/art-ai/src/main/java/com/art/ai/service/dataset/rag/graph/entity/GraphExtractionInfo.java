package com.art.ai.service.dataset.rag.graph.entity;

import lombok.Getter;

/**
 * @author fxz
 * @since 2025/9/16 20:17
 */
public record GraphExtractionInfo(@Getter GraphEntityDesc entityDesc, @Getter GraphEntityRelation entityRelation) {
}
