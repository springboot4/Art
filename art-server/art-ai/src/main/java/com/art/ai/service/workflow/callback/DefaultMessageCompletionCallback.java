package com.art.ai.service.workflow.callback;

import com.art.ai.core.dto.conversation.SaveMessageDTO;
import com.art.ai.service.message.MessageService;
import com.art.core.common.util.AsyncUtil;
import com.art.core.common.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 默认的消息完成回调实现
 *
 * @author fxz
 */
@Slf4j
public class DefaultMessageCompletionCallback implements MessageCompletionCallback {

	@Override
	public void onComplete(MessageCompletionEvent event) {
		if (event == null) {
			log.warn("消息完成事件为空，跳过处理");
			return;
		}

		if (event.getConversationId() == null) {
			log.error("会话ID为空，无法保存消息，nodeId={}", event.getNodeId());
			return;
		}

		if (event.getContent() == null) {
			log.warn("消息内容为空，nodeId={}", event.getNodeId());
		}

		try {
			log.info("保存LLM生成的消息，nodeId={}, conversationId={}, contentLength={}", event.getNodeId(),
					event.getConversationId(), event.getContent() != null ? event.getContent().length() : 0);

			// 构建保存消息VO
			SaveMessageDTO saveMessageDTO = SaveMessageDTO.builder()
				.conversationId(event.getConversationId())
				.instanceId(event.getInstanceId())
				.role(event.getRole().getCode())
				.content(event.getContent())
				.modelProvider(event.getModelProvider())
				.modelId(event.getModelId())
				.promptTokens(event.getPromptTokens() != null ? event.getPromptTokens() : 0)
				.completionTokens(event.getCompletionTokens() != null ? event.getCompletionTokens() : 0)
				.totalTokens(event.getTotalTokens() != null ? event.getTotalTokens() : 0)
				.totalCost(BigDecimal.ZERO)
				.build();

			// 保存消息
			AsyncUtil.run(() -> {
				Long messageId = SpringUtil.getBean(MessageService.class).saveMessage(saveMessageDTO);
				log.info("消息保存成功，messageId={}, nodeId={}", messageId, event.getNodeId());
			});
		}
		catch (Exception e) {
			log.error("保存消息失败，nodeId={}, conversationId={}", event.getNodeId(), event.getConversationId(), e);
		}
	}

}
