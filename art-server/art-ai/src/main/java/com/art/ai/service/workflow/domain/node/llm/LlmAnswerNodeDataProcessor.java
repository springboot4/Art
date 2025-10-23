package com.art.ai.service.workflow.domain.node.llm;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.service.model.runtime.AiStreamingModelRuntimeService;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.callback.MessageCompletionCallback;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
import com.art.ai.service.workflow.domain.node.llm.memory.ChatMemoryService;
import com.art.ai.service.workflow.variable.VariablePoolManager;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import com.art.core.common.util.SpringUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.state.AgentState;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支持流式输出的大模型答案节点。
 *
 * @author fxz
 */
@Slf4j
public class LlmAnswerNodeDataProcessor extends NodeDataProcessor<LlmAnswerNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());
		LlmNodeConfig nodeConfig = getConfig();

		List<ChatMessage> messages = buildMessages(nodeConfig, inputs, workFlowContext);
		if (messages.isEmpty()) {
			log.warn("LLM answer node [{}] 没有可用对话消息", workFlowContext.getCurrentNodeId());
		}

		AiStreamingModelRuntimeService streamingRuntimeService = SpringUtil
			.getBean(AiStreamingModelRuntimeService.class);
		StreamingChatModel streamingChatModel = streamingRuntimeService.acquireStreamingChatModel(null,
				Long.valueOf(nodeConfig.getModel()), buildInvokeOptions(nodeConfig));

		ChatRequestParameters parameters = ChatRequestParameters.builder()
			.temperature(nodeConfig.getTemperature())
			.maxOutputTokens(nodeConfig.getMaxTokens())
			.build();

		ChatRequest chatRequest = ChatRequest.builder().messages(messages).parameters(parameters).build();

		String nodeId = workFlowContext.getCurrentNodeId();
		String nodeLabel = workFlowContext.getCurrentNodeLabel();

		StreamingChatGenerator<AgentState> generator = StreamingChatGenerator.builder()
			.startingNode(nodeId)
			.startingState(nodeState)
			.mapResult(response -> onCompletion(response, nodeId, nodeLabel, workFlowContext))
			.build();

		streamingChatModel.chat(chatRequest, generator.handler());

		return NodeProcessResult.builder().outputVariables(List.of()).streamingOutputs(generator).build();
	}

	private Map<String, Object> onCompletion(ChatResponse response, String nodeId, String nodeLabel,
			WorkFlowContext workFlowContext) {
		if (response == null || response.aiMessage() == null) {
			log.error("LLM answer node [{}] 响应为空", nodeId);
			return buildErrorResult(nodeId, nodeLabel, "LLM响应为空");
		}

		String text = response.aiMessage().text();
		log.debug("LLM answer node [{}] 完成生成，内容长度: {}", nodeId, text != null ? text.length() : 0);

		// 输出变量
		List<NodeOutputVariable> outputs = List
			.of(new NodeOutputVariable(WorkflowNode.NodeOutputConstants.OUTPUT, "string", text));

		// 构建结果
		HashMap<String, Object> result = new HashMap<>();
		result.put("outputs", outputs);
		result.put("nodeName", nodeLabel);
		result.put("nodeId", nodeId);
		result.put("status", "completed");

		// 更新变量池
		try {
			outputs.forEach(variable -> VariablePoolManager.updateNodeOutputVariable(workFlowContext.getCurrentNodeId(),
					variable.getName(), variable.getValue(), workFlowContext.getPool()));
		}
		catch (Exception e) {
			log.error("更新变量池失败，nodeId={}", nodeId, e);
		}

		// 触发消息完成回调
		try {
			triggerMessageCompletionCallback(workFlowContext, nodeId, nodeLabel, text, response);
		}
		catch (Exception e) {
			log.error("触发消息完成回调失败，nodeId={}", nodeId, e);
		}

		return result;
	}

	/**
	 * 构建错误结果
	 */
	private Map<String, Object> buildErrorResult(String nodeId, String nodeLabel, String errorMessage) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("nodeId", nodeId);
		result.put("nodeName", nodeLabel);
		result.put("status", "failed");
		result.put("error", errorMessage);
		return result;
	}

	private List<ChatMessage> buildMessages(LlmNodeConfig nodeConfig, Map<String, Object> inputs,
			WorkFlowContext workFlowContext) {
		List<ChatMessage> chatMessages = new ArrayList<>();

		// 1. System Prompt
		String systemPrompt = nodeConfig.getSystemPrompt();
		if (StringUtils.isNotBlank(systemPrompt)) {
			chatMessages.add(SystemMessage.from(VariableRenderUtils.format(systemPrompt, inputs)));
		}

		// 2. 手动插入的消息
		if (CollectionUtil.isNotEmpty(nodeConfig.getMessages())) {
			nodeConfig.getMessages().stream().filter(m -> StringUtils.isNoneBlank(m.getContent())).forEach(message -> {
				String content = VariableRenderUtils.format(message.getContent(), inputs);
				if (StringUtils.equals(message.getRole(), MessageRoleEnum.USER.getCode())) {
					chatMessages.add(UserMessage.from(content));
				}
				else if (StringUtils.equals(message.getRole(), MessageRoleEnum.ASSISTANT.getCode())) {
					chatMessages.add(AiMessage.from(content));
				}
			});
		}

		// 3. 记忆消息（历史对话）
		if (nodeConfig.getMemory() != null && Boolean.TRUE.equals(nodeConfig.getMemory().getEnabled())) {
			Long conversationId = extractConversationId(workFlowContext);
			if (conversationId != null) {
				ChatMemoryService memoryService = new ChatMemoryService();
				List<ChatMessage> memoryMessages = memoryService.getHistoryMessages(conversationId,
						nodeConfig.getMemory());
				chatMessages.addAll(memoryMessages);
			}
		}

		return chatMessages;
	}

	private AiModelInvokeOptions buildInvokeOptions(LlmNodeConfig nodeConfig) {
		AiModelInvokeOptions.Builder builder = AiModelInvokeOptions.builder();
		if (nodeConfig.getTimeout() > 0) {
			builder.timeout(Duration.ofSeconds(nodeConfig.getTimeout()));
		}
		if (nodeConfig.getTemperature() != null) {
			builder.temperature(BigDecimal.valueOf(nodeConfig.getTemperature()));
		}
		if (nodeConfig.getMaxTokens() != null) {
			builder.maxOutputTokens(nodeConfig.getMaxTokens());
		}
		return builder.build();
	}

	@Override
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
			.modelId(getConfig().getModel())
			.modelProvider(getConfig().getModel())
			.promptTokens(promptTokens)
			.completionTokens(completionTokens)
			.totalTokens(totalTokens)
			.build();
	}

}
