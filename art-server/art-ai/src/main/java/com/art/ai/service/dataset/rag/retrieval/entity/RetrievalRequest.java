package com.art.ai.service.dataset.rag.retrieval.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

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

	private List<Long> datasetIds;

	private List<RetrievalType> retrievalTypes;

	/**
	 * 获取所有数据集ID
	 */
	public List<Long> getAllDatasetIds() {
		if (CollectionUtils.isNotEmpty(datasetIds)) {
			return datasetIds;
		}

		if (datasetId != null) {
			return List.of(datasetId);
		}

		return List.of();
	}

}