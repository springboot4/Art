package com.art.ai.service.workflow.domain.node.llm;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.service.model.runtime.AiModelRuntimeService;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 15:39
 */
@Slf4j
@Data
public class LlmNodeDataProcessor extends NodeDataProcessor<LlmNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());
		LlmNodeConfig nodeConfig = getConfig();

		List<LlmNodeConfig.Message> messages = nodeConfig.getMessages();
		String systemPrompt = nodeConfig.getSystemPrompt();

		AiModelRuntimeService modelRuntimeService = SpringUtil.getBean(AiModelRuntimeService.class);
		AiModelInvokeOptions options = AiModelInvokeOptions.builder()
			.timeout(Duration.ofSeconds(nodeConfig.getTimeout()))
			.build();
		ChatModel chatModel = modelRuntimeService.acquireChatModel(null, Long.valueOf(nodeConfig.getModel()), options);

		ChatRequestParameters parameters = ChatRequestParameters.builder()
			.temperature(nodeConfig.getTemperature())
			.maxOutputTokens(nodeConfig.getMaxTokens())
			.build();

		List<ChatMessage> chatMessages = new ArrayList<>();
		if (StringUtils.isNoneBlank(systemPrompt)) {
			SystemMessage systemMessage = SystemMessage.from(VariableRenderUtils.format(systemPrompt, inputs));
			chatMessages.add(systemMessage);
		}

		if (CollectionUtil.isNotEmpty(messages)) {
			messages.forEach(message -> {
				if (StringUtils.equals(message.getRole(), MessageRoleEnum.USER.getCode())) {
					chatMessages.add(UserMessage.from(VariableRenderUtils.format(message.getContent(), inputs)));
				}
				else if (StringUtils.equals(message.getRole(), MessageRoleEnum.ASSISTANT.getCode())) {
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
