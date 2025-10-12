package com.art.ai.core.convert;

import com.art.ai.core.dto.qa.AiQaPairsDTO;
import com.art.ai.dao.dataobject.AiQaPairsDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * QA问答对转换器
 *
 * @author fxz
 * @since 2025/10/12
 */
@Mapper
public interface AiQaPairsConvert {

	AiQaPairsConvert INSTANCE = Mappers.getMapper(AiQaPairsConvert.class);

	Page<AiQaPairsDTO> convertPage(Page<AiQaPairsDO> page);

	List<AiQaPairsDTO> convertList(List<AiQaPairsDO> list);

	AiQaPairsDTO convert(AiQaPairsDO dataObject);

	AiQaPairsDO convert(AiQaPairsDTO dto);

}
