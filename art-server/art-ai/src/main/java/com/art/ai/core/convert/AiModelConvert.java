package com.art.ai.core.convert;

import com.art.ai.core.dto.AiModelDTO;
import com.art.ai.dao.dataobject.AiModelDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-10-09
 */
@Mapper
public interface AiModelConvert {

	AiModelConvert INSTANCE = Mappers.getMapper(AiModelConvert.class);

	Page<AiModelDTO> convertPage(Page<AiModelDO> aiModelDO);

	List<AiModelDTO> convertList(List<AiModelDO> aiModelDO);

	AiModelDTO convert(AiModelDO aiModelDO);

	AiModelDO convert(AiModelDTO aiModelDTO);

}