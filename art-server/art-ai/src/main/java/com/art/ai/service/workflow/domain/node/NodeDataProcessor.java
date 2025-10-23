package com.art.ai.service.workflow.domain.node;

import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.callback.MessageCompletionCallback;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariableSelector;
import com.art.ai.service.workflow.variable.VariableValue;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fxz
 * @since 2025/8/24 17:18
 */
@Data
@Slf4j
public abstract class NodeDataProcessor<C extends NodeConfig> extends NodeData<C> {

	public abstract NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState);

	protected Map<String, Object> initNodeInputsByReference(VariablePool variablePool, C nodeConfig) {
		List<NodeReferenceParameter> referenceParameters = nodeConfig.getReferenceParameters();
		if (referenceParameters == null || referenceParameters.isEmpty()) {
			return Map.of();
		}

		return referenceParameters.stream()
			.collect(Collectors.toMap(NodeReferenceParameter::formatVariableName,
					r -> variablePool.get(VariableSelector.of(r.variableType(), r.nodeId(), r.parameterName()))
						.orElseThrow(() -> new RuntimeException("Variable not found for " + r.parameterName()))
						.getValue()));
	}

	/**
	 * 触发消息完成回调
	 * @param workFlowContext 工作流上下文
	 * @param nodeId 节点ID
	 * @param nodeLabel 节点名称
	 * @param content 生成的内容
	 * @param response LLM响应
	 */
	protected void triggerMessageCompletionCallback(WorkFlowContext workFlowContext, String nodeId, String nodeLabel,
			String content, ChatResponse response) {
		if (workFlowContext == null) {
			log.warn("WorkFlowContext为空，无法触发消息完成回调");
			return;
		}

		// 获取回调
		MessageCompletionCallback callback = workFlowContext.getMessageCompletionCallback();
		if (callback == null) {
			log.debug("未设置消息完成回调!");
			return;
		}

		// 获取会话ID
		Long conversationId = extractConversationId(workFlowContext);
		if (conversationId == null) {
			throw new IllegalStateException("无法获取会话ID");
		}

		// 获取实例ID
		Long instanceId = workFlowContext.getRuntime() != null ? workFlowContext.getRuntime().getId() : null;

		Integer promptTokens = null;
		Integer completionTokens = null;
		Integer totalTokens = null;
		if (response != null && response.tokenUsage() != null) {
			promptTokens = response.tokenUsage().inputTokenCount();
			completionTokens = response.tokenUsage().outputTokenCount();
			totalTokens = response.tokenUsage().totalTokenCount();
		}

		// 构建事件
		MessageCompletionCallback.MessageCompletionEvent event = getMessageCompletionEvent(nodeId, nodeLabel, content,
				conversationId, instanceId, promptTokens, completionTokens, totalTokens);

		// 调用回调
		callback.onComplete(event);
	}

	protected MessageCompletionCallback.MessageCompletionEvent getMessageCompletionEvent(String nodeId,
			String nodeLabel, String content, Long conversationId, Long instanceId, Integer promptTokens,
			Integer completionTokens, Integer totalTokens) {
		return MessageCompletionCallback.MessageCompletionEvent.builder()
			.nodeId(nodeId)
			.nodeLabel(nodeLabel)
			.content(content)
			.role(MessageRoleEnum.ASSISTANT)
			.conversationId(conversationId)
			.instanceId(instanceId)
			.promptTokens(promptTokens)
			.completionTokens(completionTokens)
			.totalTokens(totalTokens)
			.build();
	}

	/**
	 * 从上下文中提取会话ID
	 */
	protected Long extractConversationId(WorkFlowContext workFlowContext) {
		try {
			Optional<VariableValue<?>> variableValue = workFlowContext.getPool()
				.get(VariableSelector.system(SystemVariableKey.CONVERSATION_ID));

			if (variableValue.isEmpty()) {
				log.warn("系统变量 CONVERSATION_ID 未找到");
				return null;
			}

			VariableValue<?> conversationIdVariable = variableValue.get();
			Object value = conversationIdVariable.getValue();

			if (value instanceof String) {
				return Long.valueOf((String) value);
			}
			else if (value instanceof Long) {
				return (Long) value;
			}
			else if (value instanceof Number) {
				return ((Number) value).longValue();
			}
			else {
				log.warn("会话ID类型不支持: {}", value != null ? value.getClass() : null);
				return null;
			}
		}
		catch (Exception e) {
			log.error("提取会话ID失败", e);
			return null;
		}
	}

}
