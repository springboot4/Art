package com.art.ai.service.message.impl;

import cn.hutool.core.util.StrUtil;
import com.art.ai.core.convert.AiMessageConvert;
import com.art.ai.core.dto.WorkflowRunDTO;
import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.core.dto.conversation.MessageQueryDTO;
import com.art.ai.core.dto.conversation.SaveMessageDTO;
import com.art.ai.core.dto.conversation.SendMsgDTO;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.core.enums.MessageStatusEnum;
import com.art.ai.core.enums.MessageTypeEnum;
import com.art.ai.dao.dataobject.AiMessageDO;
import com.art.ai.manager.AiMessageManager;
import com.art.ai.service.conversation.ConversationService;
import com.art.ai.service.message.MessageService;
import com.art.ai.service.workflow.WorkflowStarter;
import com.art.core.common.model.PageResult;
import com.art.core.common.util.AsyncUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.art.ai.service.workflow.variable.SystemVariableKey.CONVERSATION_ID;

/**
 * AI消息服务实现
 *
 * @author fxz
 * @date 2025-10-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final AiMessageManager messageManager;

	private final ConversationService conversationService;

	private final WorkflowStarter workflowStarter;

	@Lazy
	@Resource
	private MessageService messageService;

	@Override
	public SseEmitter sendMessages(SendMsgDTO sendMsgDTO) {
		String conversationId = sendMsgDTO.getConversationId();
		AsyncUtil.run(() -> {
			SaveMessageDTO messageDTO = SaveMessageDTO.builder()
				.content(sendMsgDTO.getUserQuery())
				.role(MessageRoleEnum.USER.getCode())
				.conversationId(Long.valueOf(conversationId))
				.build();
			messageService.saveMessage(messageDTO);
		});

		WorkflowRunDTO workflowRunInfo = sendMsgDTO.getWorkflowRunInfo();
		workflowRunInfo.getSystems().put(CONVERSATION_ID.getKey(), conversationId);
		return workflowStarter.streaming(workflowRunInfo.getWorkflowId(), workflowRunInfo.getInputs(),
				workflowRunInfo.getSystems());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveMessage(SaveMessageDTO vo) {
		log.info("保存消息，conversationId={}, role={}", vo.getConversationId(), vo.getRole());

		AiMessageDO messageDO = new AiMessageDO();
		messageDO.setMessageUuid(UUID.randomUUID().toString());
		messageDO.setConversationId(vo.getConversationId());
		messageDO.setInstanceId(vo.getInstanceId());
		messageDO.setInstanceType(vo.getInstanceType());
		messageDO.setRole(vo.getRole());
		messageDO.setMessageType(
				StrUtil.isNotBlank(vo.getMessageType()) ? vo.getMessageType() : MessageTypeEnum.TEXT.getCode());
		messageDO.setContent(vo.getContent());
		messageDO.setModelProvider(vo.getModelProvider());
		messageDO.setModelId(vo.getModelId());
		messageDO.setPromptTokens(vo.getPromptTokens() != null ? vo.getPromptTokens() : 0);
		messageDO.setCompletionTokens(vo.getCompletionTokens() != null ? vo.getCompletionTokens() : 0);
		messageDO.setTotalTokens(vo.getTotalTokens() != null ? vo.getTotalTokens() : 0);
		messageDO.setTotalCost(vo.getTotalCost() != null ? vo.getTotalCost() : BigDecimal.ZERO);
		messageDO
			.setStatus(StrUtil.isNotBlank(vo.getStatus()) ? vo.getStatus() : MessageStatusEnum.COMPLETED.getCode());
		messageDO.setErrorMessage(vo.getErrorMessage());
		messageDO.setMetadata(vo.getMetadata());
		messageDO.setCreateTime(LocalDateTime.now());

		if (MessageStatusEnum.COMPLETED.getCode().equals(messageDO.getStatus())) {
			messageDO.setCompletedAt(LocalDateTime.now());
		}

		messageManager.insert(messageDO);

		try {
			conversationService.updateStats(vo.getConversationId(), 1, messageDO.getTotalTokens(),
					messageDO.getTotalCost());
		}
		catch (Exception e) {
			log.error("更新会话统计失败，conversationId={}", vo.getConversationId(), e);
		}

		log.info("保存消息成功，messageId={}, uuid={}", messageDO.getId(), messageDO.getMessageUuid());

		return messageDO.getId();
	}

	@Override
	public List<AiMessageDTO> getRecentMessages(Long conversationId, int limit) {
		log.debug("查询最近消息，conversationId={}, limit={}", conversationId, limit);

		List<AiMessageDO> messageDOList = messageManager.selectRecentMessages(conversationId, limit);

		return AiMessageConvert.INSTANCE.convertList(messageDOList);
	}

	@Override
	public PageResult<AiMessageDTO> page(MessageQueryDTO queryVO) {
		Page<AiMessageDO> page = messageManager.page(queryVO.getCurrent(), queryVO.getSize(),
				queryVO.getConversationId(), queryVO.getRole(), queryVO.getStatus());

		return PageResult.success(AiMessageConvert.INSTANCE.convertPage(page));
	}

}
