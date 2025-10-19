package com.art.ai.service.workflow.domain.node.llm.memory;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.service.message.MessageService;
import com.art.ai.service.workflow.domain.node.llm.config.MemoryConfig;
import com.art.core.common.util.SpringUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话记忆服务
 * <p>
 * 负责从数据库获取历史消息并转换为 LangChain4j 的 ChatMessage 格式
 *
 * @author fxz
 * @date 2025-10-19
 */
@Slf4j
public class ChatMemoryService {

	private final MessageService messageService;

	public ChatMemoryService() {
		this.messageService = SpringUtil.getBean(MessageService.class);
	}

	/**
	 * 获取会话的历史消息
	 * @param conversationId 会话ID
	 * @param memoryConfig 记忆配置
	 * @return ChatMessage 列表（按时间正序）
	 */
	public List<ChatMessage> getHistoryMessages(Long conversationId, MemoryConfig memoryConfig) {
		// 参数校验
		if (conversationId == null) {
			log.warn("会话ID为空，无法获取历史消息");
			return new ArrayList<>();
		}

		if (memoryConfig == null || !Boolean.TRUE.equals(memoryConfig.getEnabled())) {
			log.debug("记忆功能未启用，会话ID: {}", conversationId);
			return new ArrayList<>();
		}

		try {
			// 获取窗口大小
			int windowSize = getWindowSize(memoryConfig);

			// 从数据库查询历史消息
			List<AiMessageDTO> messages = messageService.getRecentMessages(conversationId, windowSize);

			if (CollectionUtil.isEmpty(messages)) {
				log.debug("会话 {} 没有历史消息", conversationId);
				return new ArrayList<>();
			}

			// 转换为 ChatMessage
			List<ChatMessage> chatMessages = convertToChatMessages(messages);

			log.debug("会话 {} 加载了 {} 条历史消息", conversationId, chatMessages.size());

			return chatMessages;
		}
		catch (Exception e) {
			log.error("获取会话 {} 的历史消息失败", conversationId, e);
			return new ArrayList<>();
		}
	}

	/**
	 * 获取窗口大小
	 */
	private int getWindowSize(MemoryConfig memoryConfig) {
		if (memoryConfig.getWindow() != null && memoryConfig.getWindow().getSize() != null) {
			return memoryConfig.getWindow().getSize();
		}
		return 10;
	}

	/**
	 * 转换消息为 ChatMessage 列表
	 * @param messages AI消息列表
	 * @return ChatMessage 列表
	 */
	private List<ChatMessage> convertToChatMessages(List<AiMessageDTO> messages) {
		List<ChatMessage> chatMessages = new ArrayList<>();

		for (AiMessageDTO msg : messages) {
			if (msg.getRole() == null || msg.getContent() == null) {
				log.warn("消息ID {} 的角色或内容为空，跳过", msg.getId());
				continue;
			}

			try {
				if (MessageRoleEnum.USER.getCode().equals(msg.getRole())) {
					chatMessages.add(UserMessage.from(msg.getContent()));
				}
				else if (MessageRoleEnum.ASSISTANT.getCode().equals(msg.getRole())) {
					chatMessages.add(AiMessage.from(msg.getContent()));
				}
				else {
					log.warn("不支持的消息角色: {}, 消息ID: {}", msg.getRole(), msg.getId());
				}
			}
			catch (Exception e) {
				log.error("转换消息失败，消息ID: {}", msg.getId(), e);
			}
		}

		return chatMessages;
	}

}
