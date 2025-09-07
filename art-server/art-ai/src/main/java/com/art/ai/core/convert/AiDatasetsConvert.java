package com.art.ai.core.convert;

import com.art.ai.core.dto.AiDatasetsDTO;
import com.art.ai.dao.dataobject.AiDatasetsDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Mapper
public interface AiDatasetsConvert {

	AiDatasetsConvert INSTANCE = Mappers.getMapper(AiDatasetsConvert.class);

	Page<AiDatasetsDTO> convertPage(Page<AiDatasetsDO> aiDatasetsDO);

	List<AiDatasetsDTO> convertList(List<AiDatasetsDO> aiDatasetsDO);

	AiDatasetsDTO convert(AiDatasetsDO aiDatasetsDO);

	AiDatasetsDO convert(AiDatasetsDTO aiDatasetsDTO);

}