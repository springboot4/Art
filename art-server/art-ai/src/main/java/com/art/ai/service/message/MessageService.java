package com.art.ai.service.message;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.core.dto.conversation.MessageQueryDTO;
import com.art.ai.core.dto.conversation.SaveMessageDTO;
import com.art.ai.core.dto.conversation.SendMsgDTO;
import com.art.core.common.model.PageResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * AI消息服务接口
 *
 * @author fxz
 * @date 2025-10-18
 */
public interface MessageService {

	/**
	 * 发送消息
	 */
	SseEmitter sendMessages(SendMsgDTO sendMsgDTO);

	/**
	 * 保存消息
	 * @param vo 消息信息
	 * @return 消息ID
	 */
	Long saveMessage(SaveMessageDTO vo);

	/**
	 * 获取会话的最近N条消息
	 * @param conversationId 会话ID
	 * @param limit 数量限制
	 * @return 消息列表（按时间正序）
	 */
	List<AiMessageDTO> getRecentMessages(Long conversationId, int limit);

	/**
	 * 分页查询消息
	 * @param queryVO 查询条件
	 * @return 分页结果
	 */
	PageResult<AiMessageDTO> page(MessageQueryDTO queryVO);

}
