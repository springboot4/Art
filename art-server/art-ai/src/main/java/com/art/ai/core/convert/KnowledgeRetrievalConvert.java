package com.art.ai.core.convert;

import com.art.ai.core.dto.retrieval.KnowledgeRetrievalDTO;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 知识库召回转换器
 *
 * @author fxz
 * @since 2025/10/05
 */
@Mapper
public interface KnowledgeRetrievalConvert {

	KnowledgeRetrievalConvert INSTANCE = Mappers.getMapper(KnowledgeRetrievalConvert.class);

	RetrievalRequest toRequest(KnowledgeRetrievalDTO dto);

}