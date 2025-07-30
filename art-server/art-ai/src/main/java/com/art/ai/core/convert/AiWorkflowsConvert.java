package com.art.ai.core.convert;

import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.dao.dataobject.AiWorkflowsDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-07-31
 */
@Mapper
public interface AiWorkflowsConvert {

	AiWorkflowsConvert INSTANCE = Mappers.getMapper(AiWorkflowsConvert.class);

	Page<AiWorkflowsDTO> convertPage(Page<AiWorkflowsDO> aiWorkflowsDO);

	List<AiWorkflowsDTO> convertList(List<AiWorkflowsDO> aiWorkflowsDO);

	AiWorkflowsDTO convert(AiWorkflowsDO aiWorkflowsDO);

	AiWorkflowsDO convert(AiWorkflowsDTO aiWorkflowsDTO);

}