package com.art.ai.core.convert;

import com.art.ai.core.dto.AiModelPlatformDTO;
import com.art.ai.dao.dataobject.AiModelPlatformDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-10-09
 */
@Mapper
public interface AiModelPlatformConvert {

	AiModelPlatformConvert INSTANCE = Mappers.getMapper(AiModelPlatformConvert.class);

	Page<AiModelPlatformDTO> convertPage(Page<AiModelPlatformDO> aiModelPlatformDO);

	List<AiModelPlatformDTO> convertList(List<AiModelPlatformDO> aiModelPlatformDO);

	AiModelPlatformDTO convert(AiModelPlatformDO aiModelPlatformDO);

	AiModelPlatformDO convert(AiModelPlatformDTO aiModelPlatformDTO);

}