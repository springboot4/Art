package com.art.ai.core.convert;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.dao.dataobject.AiAgentDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Agent 转换器
 *
 * @author fxz
 * @since 2025-11-01
 */
@Mapper
public interface AiAgentConvert {

	AiAgentConvert INSTANCE = Mappers.getMapper(AiAgentConvert.class);

	AiAgentDTO convert(AiAgentDO entity);

	AiAgentDO convert(AiAgentDTO dto);

	List<AiAgentDTO> convertList(List<AiAgentDO> list);

	Page<AiAgentDTO> convertPage(Page<AiAgentDO> page);

}
