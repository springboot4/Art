package com.art.ai.service.conversation;

import com.art.ai.core.dto.conversation.AiConversationDTO;
import com.art.ai.core.dto.conversation.ConversationCreateDTO;
import com.art.ai.core.dto.conversation.ConversationQueryDTO;
import com.art.core.common.model.PageResult;

import java.math.BigDecimal;

/**
 * AI会话服务接口
 *
 * @author fxz
 * @date 2025-10-18
 */
public interface ConversationService {

	/**
	 * 创建会话
	 * @param vo 创建参数
	 * @return 会话信息
	 */
	AiConversationDTO create(ConversationCreateDTO vo);

	/**
	 * 分页查询会话列表
	 * @param queryVO 查询条件
	 * @return 分页结果
	 */
	PageResult<AiConversationDTO> page(ConversationQueryDTO queryVO);

	/**
	 * 更新会话标题
	 * @param conversationId 会话ID
	 * @param name 新标题
	 */
	void updateName(Long conversationId, String name);

	/**
	 * 更新统计信息（消息数、Token、成本、最后消息时间）
	 * @param conversationId 会话ID
	 * @param messageDelta 消息增量
	 * @param tokensDelta Token增量
	 * @param costDelta 成本增量
	 */
	void updateStats(Long conversationId, int messageDelta, int tokensDelta, BigDecimal costDelta);

}
