package com.art.ai.service.agent;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.conversation.AgentRunDTO;
import com.art.ai.core.dto.conversation.SaveMessageDTO;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.core.sse.SSEEmitterHelper;
import com.art.ai.service.agent.runtime.AgentExecutor;
import com.art.ai.service.agent.runtime.AgentProgressMessage;
import com.art.ai.service.agent.runtime.AgentRunRequest;
import com.art.ai.service.agent.runtime.AgentRunResult;
import com.art.ai.service.message.MessageService;
import com.art.ai.service.workflow.callback.CallbackData;
import com.art.ai.service.workflow.domain.node.NodeStatus;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.common.tenant.context.TenantContextHolder;
import com.art.core.common.exception.ArtException;
import com.art.core.common.util.AsyncUtil;
import com.art.json.sdk.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * Agent 应用服务
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentApplicationService {

	private final AgentService agentService;

	private final AgentExecutor agentExecutor;

	private final MessageService messageService;

	public SseEmitter run(AgentRunDTO dto) {
		AiAgentDTO agent = agentService.findById(dto.getAgentId());
		if (agent == null) {
			throw new ArtException("Agent 不存在: " + dto.getAgentId());
		}
		SseEmitter emitter = new SseEmitter(150000L);
		SSEEmitterHelper.sendStart(emitter);

		Long tenantId = TenantContextHolder.getTenantId();
		Authentication authentication = SecurityUtil.getAuthentication();

		AsyncUtil.run(() -> {
			try {
				TenantContextHolder.setTenantId(tenantId);
				SecurityUtil.setAuthentication(authentication);

				saveUserMessage(dto);

				AgentRunRequest runRequest = AgentRunRequest.builder()
					.agentId(dto.getAgentId())
					.runId(dto.getRunId())
					.conversationId(dto.getConversationId())
					.input(dto.getUserQuery())
					.variables(dto.getVariables() != null ? dto.getVariables() : Collections.emptyMap())
					.build();

				AgentRunResult result = agentExecutor.run(agent, runRequest, (type, data) -> {
					String progressData = JacksonUtil.toJsonString(AgentProgressMessage.of(type, data));
					SSEEmitterHelper.parseAndSendPartialMsg(emitter, progressData);
				});

				String outputData = JacksonUtil.toJsonString(CallbackData.builder()
					.nodeId(result.getRunId())
					.chunk(StringUtils.defaultString(result.getOutput()))
					.nodeStatus(NodeStatus.NODE_STATUS_RUNNING)
					.build());
				SSEEmitterHelper.parseAndSendPartialMsg(emitter, outputData);
				SSEEmitterHelper.sendComplete(emitter);

				saveAssistantMessage(dto, agent, result);
			}
			catch (Exception ex) {
				log.error("Agent 执行异常", ex);
				SSEEmitterHelper.sendErrorAndComplete(emitter, ex.getMessage());
			}
			finally {
				TenantContextHolder.clear();
				SecurityUtil.setAuthentication(null);
			}
		});

		return emitter;
	}

	private void saveUserMessage(AgentRunDTO dto) {
		SaveMessageDTO userMessage = SaveMessageDTO.builder()
			.conversationId(dto.getConversationId())
			.role(MessageRoleEnum.USER.getCode())
			.content(dto.getUserQuery())
			.instanceId(dto.getAgentId())
			.instanceType("agent")
			.build();
		messageService.saveMessage(userMessage);
	}

	private void saveAssistantMessage(AgentRunDTO dto, AiAgentDTO agent, AgentRunResult result) {
		SaveMessageDTO assistantMessage = SaveMessageDTO.builder()
			.conversationId(dto.getConversationId())
			.role(MessageRoleEnum.ASSISTANT.getCode())
			.content(StringUtils.defaultString(result.getOutput()))
			.instanceId(agent.getId())
			.instanceType("agent")
			.promptTokens(Math.toIntExact(result.getPromptTokens()))
			.completionTokens(Math.toIntExact(result.getCompletionTokens()))
			.totalTokens(Math.toIntExact(result.getTotalTokens()))
			.totalCost(BigDecimal.ZERO)
			.build();
		messageService.saveMessage(assistantMessage);
	}

}
