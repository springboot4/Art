package com.art.ai.service.workflow.domain.node.llm;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import com.art.core.common.util.SpringUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 15:39
 */
@Slf4j
@Data
public class LlmNodeData extends NodeData<LlmNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());
		LlmNodeConfig nodeConfig = getConfig();

		String model = nodeConfig.getModel();
		List<LlmNodeConfig.Message> messages = nodeConfig.getMessages();
		String systemPrompt = nodeConfig.getSystemPrompt();
		Integer maxTokens = nodeConfig.getMaxTokens();
		Double temperature = nodeConfig.getTemperature();

		// todo fxz 模型管理tmp:
		ChatModel chatModel = OpenAiChatModel.builder()
			.baseUrl(SpringUtil.getProperty("tmp.open-ai.base-url"))
			.apiKey(SpringUtil.getProperty("tmp.open-ai.api-key"))
			.logRequests(true)
			.build();

		ChatRequestParameters parameters = ChatRequestParameters.builder()
			.modelName(model)
			.temperature(temperature)
			.maxOutputTokens(maxTokens)
			.build();

		List<ChatMessage> chatMessages = new ArrayList<>();
		if (StringUtils.isNoneBlank(systemPrompt)) {
			SystemMessage systemMessage = SystemMessage.from(VariableRenderUtils.format(systemPrompt, inputs));
			chatMessages.add(systemMessage);
		}

		if (CollectionUtil.isNotEmpty(messages)) {
			messages.forEach(message -> {
				if (StringUtils.equals(message.getRole(), LlmNodeConfig.MessageRole.USER)) {
					chatMessages.add(UserMessage.from(VariableRenderUtils.format(message.getContent(), inputs)));
				}
				else if (StringUtils.equals(message.getRole(), LlmNodeConfig.MessageRole.ASSISTANT)) {
					chatMessages.add(AiMessage.from(VariableRenderUtils.format(message.getContent(), inputs)));
				}
			});
		}

		ChatRequest chatRequest = ChatRequest.builder().messages(chatMessages).parameters(parameters).build();

		ChatResponse chatResponse = chatModel.chat(chatRequest);

		List<NodeOutputVariable> nodeOutputVariables = List.of(new NodeOutputVariable(
				WorkflowNode.NodeOutputConstants.OUTPUT, "string", chatResponse.aiMessage().text()));
		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

}
