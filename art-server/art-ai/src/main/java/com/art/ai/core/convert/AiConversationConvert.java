package com.art.ai.core.convert;

import com.art.ai.core.dto.conversation.AiConversationDTO;
import com.art.ai.dao.dataobject.AiConversationDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * AI会话转换器
 *
 * @author fxz
 * @date 2025-10-18
 */
@Mapper
public interface AiConversationConvert {

	AiConversationConvert INSTANCE = Mappers.getMapper(AiConversationConvert.class);

	/**
	 * 分页转换
	 */
	Page<AiConversationDTO> convertPage(Page<AiConversationDO> page);

	/**
	 * 列表转换
	 */
	List<AiConversationDTO> convertList(List<AiConversationDO> list);

	/**
	 * DO转DTO
	 */
	AiConversationDTO convert(AiConversationDO conversationDO);

	/**
	 * DTO转DO
	 */
	AiConversationDO convert(AiConversationDTO conversationDTO);

}
