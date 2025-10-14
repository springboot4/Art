package com.art.ai.service.workflow.domain.node.llm;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.service.model.runtime.AiStreamingModelRuntimeService;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
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
public class LlmAnswerNodeDataProcessor extends NodeDataProcessor<LlmNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());
		LlmNodeConfig nodeConfig = getConfig();

		List<ChatMessage> messages = buildMessages(nodeConfig, inputs);
		if (messages.isEmpty()) {
			log.warn("LLM answer node [{}] 没有可用对话消息", workFlowContext.getCurrentNodeId());
		}

		AiStreamingModelRuntimeService streamingRuntimeService = SpringUtil
			.getBean(AiStreamingModelRuntimeService.class);
		StreamingChatModel streamingChatModel = streamingRuntimeService.acquireStreamingChatModel(null,
				parseModelId(nodeConfig.getModel()), buildInvokeOptions(nodeConfig));

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
		String text = response.aiMessage().text();
		log.debug("LLM answer node [{}] 完成生成: {}", nodeId, text);

		List<NodeOutputVariable> outputs = List
			.of(new NodeOutputVariable(WorkflowNode.NodeOutputConstants.OUTPUT, "string", text));

		VariablePoolManager.updateNodeOutputVariable(nodeId, WorkflowNode.NodeOutputConstants.OUTPUT, text,
				workFlowContext.getPool());

		HashMap<String, Object> result = new HashMap<>();
		result.put("outputs", outputs);
		result.put("nodeName", nodeLabel);
		result.put("nodeId", nodeId);
		result.put("status", "completed");
		return result;
	}

	private List<ChatMessage> buildMessages(LlmNodeConfig nodeConfig, Map<String, Object> inputs) {
		List<ChatMessage> chatMessages = new ArrayList<>();
		String systemPrompt = nodeConfig.getSystemPrompt();
		if (StringUtils.isNotBlank(systemPrompt)) {
			chatMessages.add(SystemMessage.from(VariableRenderUtils.format(systemPrompt, inputs)));
		}

		if (CollectionUtil.isNotEmpty(nodeConfig.getMessages())) {
			nodeConfig.getMessages().forEach(message -> {
				String content = VariableRenderUtils.format(message.getContent(), inputs);
				if (StringUtils.equals(message.getRole(), LlmNodeConfig.MessageRole.USER)) {
					chatMessages.add(UserMessage.from(content));
				}
				else if (StringUtils.equals(message.getRole(), LlmNodeConfig.MessageRole.ASSISTANT)) {
					chatMessages.add(AiMessage.from(content));
				}
			});
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

	private Long parseModelId(String modelId) {
		try {
			return Long.valueOf(modelId);
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentException("非法的模型 ID: " + modelId, ex);
		}
	}

}
