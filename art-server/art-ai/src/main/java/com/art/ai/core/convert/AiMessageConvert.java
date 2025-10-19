package com.art.ai.core.convert;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.dao.dataobject.AiMessageDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * AI消息转换器
 *
 * @author fxz
 * @date 2025-10-18
 */
@Mapper
public interface AiMessageConvert {

	AiMessageConvert INSTANCE = Mappers.getMapper(AiMessageConvert.class);

	/**
	 * 分页转换
	 */
	Page<AiMessageDTO> convertPage(Page<AiMessageDO> page);

	/**
	 * 列表转换
	 */
	List<AiMessageDTO> convertList(List<AiMessageDO> list);

	/**
	 * DO转DTO
	 */
	AiMessageDTO convert(AiMessageDO messageDO);

	/**
	 * DTO转DO
	 */
	AiMessageDO convert(AiMessageDTO messageDTO);

}
