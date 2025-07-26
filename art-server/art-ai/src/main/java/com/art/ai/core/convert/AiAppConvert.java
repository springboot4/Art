package com.art.ai.core.convert;

import com.art.ai.core.dto.AiAppDTO;
import com.art.ai.dao.dataobject.AiAppDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-07-25
 */
@Mapper
public interface AiAppConvert {

	AiAppConvert INSTANCE = Mappers.getMapper(AiAppConvert.class);

	Page<AiAppDTO> convertPage(Page<AiAppDO> aiAppDO);

	List<AiAppDTO> convertList(List<AiAppDO> aiAppDO);

	AiAppDTO convert(AiAppDO aiAppDO);

	AiAppDO convert(AiAppDTO aiAppDTO);

}