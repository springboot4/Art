package com.art.ai.service.dataset.rag.retrieval.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 召回响应实体
 *
 * @author fxz
 * @since 2025/10/05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrievalResponse {

	private String query;

	private List<RetrievalResult> results;

	private Integer totalCount;

	private Long costTime;

	private Map<RetrievalType, Integer> typeCount;

	private Map<String, Object> debugInfo;

}