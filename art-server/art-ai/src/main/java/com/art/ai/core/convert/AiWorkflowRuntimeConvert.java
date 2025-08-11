package com.art.ai.core.convert;

import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.dao.dataobject.AiWorkflowRuntimeDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-08-09
 */
@Mapper
public interface AiWorkflowRuntimeConvert {

	AiWorkflowRuntimeConvert INSTANCE = Mappers.getMapper(AiWorkflowRuntimeConvert.class);

	Page<AiWorkflowRuntimeDTO> convertPage(Page<AiWorkflowRuntimeDO> aiWorkflowRuntimeDO);

	List<AiWorkflowRuntimeDTO> convertList(List<AiWorkflowRuntimeDO> aiWorkflowRuntimeDO);

	AiWorkflowRuntimeDTO convert(AiWorkflowRuntimeDO aiWorkflowRuntimeDO);

	AiWorkflowRuntimeDTO convert(AiWorkflowsDTO aiWorkflowsDTO);

	AiWorkflowRuntimeDO convert(AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO);

}