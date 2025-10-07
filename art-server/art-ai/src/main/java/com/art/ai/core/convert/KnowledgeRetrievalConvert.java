package com.art.ai.core.convert;

import com.art.ai.core.dto.retrieval.KnowledgeRetrievalDTO;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import lombok.experimental.UtilityClass;

/**
 * 知识库召回转换器
 *
 * @author fxz
 * @since 2025/10/05
 */
@UtilityClass
public class KnowledgeRetrievalConvert {

	/**
	 * DTO转换为Request
	 */
	public RetrievalRequest toRequest(KnowledgeRetrievalDTO dto) {
		if (dto == null) {
			return null;
		}

		return RetrievalRequest.builder()
			.query(dto.getQuery())
			.datasetId(dto.getDatasetId())
			.retrievalTypes(dto.getRetrievalTypes())
			.build();
	}

}