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
import com.art.ai.service.agent.tool.AgentToolException;
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
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * Agent 应用服务
 * <p>
 * 负责 Agent 执行的入口协调，包括：验证、SSE 推送、消息持久化
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentApplicationService {

	// ==================== 常量定义 ====================

	/**
	 * SSE 连接超时时间
	 */
	private static final Duration SSE_TIMEOUT = Duration.ofSeconds(150);

	/**
	 * 实例类型 - Agent
	 */
	private static final String INSTANCE_TYPE_AGENT = "agent";

	// ==================== 依赖注入 ====================

	private final AgentService agentService;

	private final AgentExecutor agentExecutor;

	private final MessageService messageService;

	// ==================== 公共方法 ====================

	/**
	 * 运行 Agent
	 *
	 * @param dto 运行请求
	 * @return SSE 发射器
	 */
	public SseEmitter run(AgentRunDTO dto) {
		// 1. 验证并加载 Agent
		AiAgentDTO agent = loadAndValidateAgent(dto.getAgentId());

		// 2. 创建 SSE 连接
		SseEmitter emitter = createSseEmitter();

		// 3. 异步执行
		executeAsync(agent, dto, emitter);

		return emitter;
	}

	// ==================== 私有方法 ====================

	/**
	 * 加载并验证 Agent
	 */
	private AiAgentDTO loadAndValidateAgent(Long agentId) {
		AiAgentDTO agent = agentService.findById(agentId);
		if (agent == null) {
			throw new ArtException("Agent 不存在: " + agentId);
		}
		return agent;
	}

	/**
	 * 创建 SSE 发射器
	 */
	private SseEmitter createSseEmitter() {
		SseEmitter emitter = new SseEmitter(SSE_TIMEOUT.toMillis());
		SSEEmitterHelper.sendStart(emitter);
		return emitter;
	}

	/**
	 * 异步执行 Agent
	 */
	private void executeAsync(AiAgentDTO agent, AgentRunDTO dto, SseEmitter emitter) {
		Long tenantId = TenantContextHolder.getTenantId();
		Authentication authentication = SecurityUtil.getAuthentication();

		AsyncUtil.run(() -> executeWithContext(agent, dto, emitter, tenantId, authentication));
	}

	/**
	 * 在正确的上下文中执行 Agent
	 */
	private void executeWithContext(AiAgentDTO agent, AgentRunDTO dto, SseEmitter emitter,
			Long tenantId, Authentication authentication) {
		try {
			// 设置上下文
			TenantContextHolder.setTenantId(tenantId);
			SecurityUtil.setAuthentication(authentication);

			// 执行 Agent
			AgentRunResult result = executeAgent(agent, dto, emitter);

			// 发送最终结果
			sendFinalOutput(emitter, result);

			// 保存消息
			saveMessages(dto, agent, result);

		} catch (Exception ex) {
			handleExecutionError(emitter, ex);
		} finally {
			clearContext();
		}
	}

	/**
	 * 执行 Agent
	 */
	private AgentRunResult executeAgent(AiAgentDTO agent, AgentRunDTO dto, SseEmitter emitter)
			throws AgentToolException {
		AgentRunRequest runRequest = buildRunRequest(dto);
		return agentExecutor.run(agent, runRequest, (type, data) -> sendProgress(emitter, type, data));
	}

	/**
	 * 构建运行请求
	 */
	private AgentRunRequest buildRunRequest(AgentRunDTO dto) {
		return AgentRunRequest.builder()
				.agentId(dto.getAgentId())
				.runId(dto.getRunId())
				.conversationId(dto.getConversationId())
				.input(dto.getUserQuery())
				.variables(dto.getVariables() != null ? dto.getVariables() : Collections.emptyMap())
				.build();
	}

	/**
	 * 发送进度消息
	 */
	private void sendProgress(SseEmitter emitter, String type, Map<String, Object> data) {
		String progressData = JacksonUtil.toJsonString(AgentProgressMessage.of(type, data));
		SSEEmitterHelper.parseAndSendPartialMsg(emitter, progressData);
	}

	/**
	 * 发送最终输出
	 */
	private void sendFinalOutput(SseEmitter emitter, AgentRunResult result) {
		String outputData = JacksonUtil.toJsonString(CallbackData.builder()
				.nodeId(result.getRunId())
				.chunk(StringUtils.defaultString(result.getOutput()))
				.nodeStatus(NodeStatus.NODE_STATUS_RUNNING)
				.build());
		SSEEmitterHelper.parseAndSendPartialMsg(emitter, outputData);
		SSEEmitterHelper.sendComplete(emitter);
	}

	/**
	 * 保存用户和助手消息
	 */
	private void saveMessages(AgentRunDTO dto, AiAgentDTO agent, AgentRunResult result) {
		saveUserMessage(dto);
		saveAssistantMessage(dto, agent, result);
	}

	/**
	 * 处理执行错误
	 */
	private void handleExecutionError(SseEmitter emitter, Exception ex) {
		log.error("Agent 执行异常", ex);
		SSEEmitterHelper.sendErrorAndComplete(emitter, ex.getMessage());
	}

	/**
	 * 清理上下文
	 */
	private void clearContext() {
		TenantContextHolder.clear();
		SecurityUtil.setAuthentication(null);
	}

	// ==================== 消息持久化 ====================

	/**
	 * 保存用户消息
	 */
	private void saveUserMessage(AgentRunDTO dto) {
		SaveMessageDTO userMessage = SaveMessageDTO.builder()
				.conversationId(dto.getConversationId())
				.role(MessageRoleEnum.USER.getCode())
				.content(dto.getUserQuery())
				.instanceId(dto.getAgentId())
				.instanceType(INSTANCE_TYPE_AGENT)
				.build();
		messageService.saveMessage(userMessage);
	}

	/**
	 * 保存助手消息
	 */
	private void saveAssistantMessage(AgentRunDTO dto, AiAgentDTO agent, AgentRunResult result) {
		SaveMessageDTO assistantMessage = SaveMessageDTO.builder()
				.conversationId(dto.getConversationId())
				.role(MessageRoleEnum.ASSISTANT.getCode())
				.content(StringUtils.defaultString(result.getOutput()))
				.instanceId(agent.getId())
				.instanceType(INSTANCE_TYPE_AGENT)
				.promptTokens(Math.toIntExact(result.getPromptTokens()))
				.completionTokens(Math.toIntExact(result.getCompletionTokens()))
				.totalTokens(Math.toIntExact(result.getTotalTokens()))
				.totalCost(BigDecimal.ZERO)
				.build();
		messageService.saveMessage(assistantMessage);
	}

}
