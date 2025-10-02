package com.art.ai.service.dataset.rag.graph.entity;

import lombok.Getter;

/**
 * @author fxz
 * @since 2025/9/16 20:14
 */
public record GraphEntityRelation(@Getter String sourceEntity, @Getter String targetEntity, @Getter String relation,
		@Getter String weight) {
}
