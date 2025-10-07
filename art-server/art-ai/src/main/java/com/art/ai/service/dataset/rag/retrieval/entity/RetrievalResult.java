package com.art.ai.service.dataset.rag.retrieval.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 召回结果实体
 *
 * @author fxz
 * @since 2025/10/05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrievalResult {

	private String documentId;

	private String segmentId;

	private String content;

	private BigDecimal score;

	private RetrievalType retrievalType;

	private Map<String, Object> metadata;

}