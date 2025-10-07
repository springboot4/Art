package com.art.ai.service.dataset.rag.retrieval.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 召回请求实体
 *
 * @author fxz
 * @since 2025/10/05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrievalRequest {

	private String query;

	private Long datasetId;

	private List<RetrievalType> retrievalTypes;

}