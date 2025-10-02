package com.art.ai.core.convert;

import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.dao.dataobject.AiDocumentSegmentDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-14
 */
@Mapper
public interface AiDocumentSegmentConvert {

	AiDocumentSegmentConvert INSTANCE = Mappers.getMapper(AiDocumentSegmentConvert.class);

	Page<AiDocumentSegmentDTO> convertPage(Page<AiDocumentSegmentDO> aiDocumentGraphSegmentDO);

	List<AiDocumentSegmentDTO> convertList(List<AiDocumentSegmentDO> aiDocumentSegmentDO);

	AiDocumentSegmentDTO convert(AiDocumentSegmentDO aiDocumentSegmentDO);

	AiDocumentSegmentDO convert(AiDocumentSegmentDTO aiDocumentSegmentDTO);

}