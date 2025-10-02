package com.art.ai.core.convert;

import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.dao.dataobject.AiDocumentsDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-09
 */
@Mapper
public interface AiDocumentsConvert {

	AiDocumentsConvert INSTANCE = Mappers.getMapper(AiDocumentsConvert.class);

	Page<AiDocumentsDTO> convertPage(Page<AiDocumentsDO> aiDocumentsDO);

	List<AiDocumentsDTO> convertList(List<AiDocumentsDO> aiDocumentsDO);

	AiDocumentsDTO convert(AiDocumentsDO aiDocumentsDO);

	AiDocumentsDO convert(AiDocumentsDTO aiDocumentsDTO);

}