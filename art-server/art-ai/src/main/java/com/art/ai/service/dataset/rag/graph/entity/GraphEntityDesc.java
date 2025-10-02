package com.art.ai.service.dataset.rag.graph.entity;

import lombok.Getter;

/**
 * @author fxz
 * @since 2025/9/16 20:03
 */
public record GraphEntityDesc(@Getter String entity, @Getter String type, @Getter String description) {
}
