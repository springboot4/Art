package com.art.ai.service.workflow.domain.node.llm;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.service.model.runtime.AiModelRuntimeService;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaConfig;
import com.art.ai.service.workflow.domain.node.llm.config.StructuredOutputConfig;
import com.art.ai.service.workflow.domain.node.llm.converter.JsonSchemaConverter;
import com.art.ai.service.workflow.domain.node.llm.converter.JsonSchemaPromptBuilder;
import com.art.ai.service.workflow.domain.node.llm.extractor.JsonExtractor;
import com.art.ai.service.workflow.domain.node.llm.memory.ChatMemoryService;
import com.art.ai.service.workflow.domain.node.llm.strategy.StructuredOutputStrategy;
import com.art.ai.service.workflow.domain.node.llm.strategy.StructuredOutputStrategySelector;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import com.art.core.common.exception.ArtException;
import com.art.core.common.util.SpringUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;

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

		// 检查是否启用结构化输出
		if (isStructuredOutputEnabled(nodeConfig)) {
			return processWithStructuredOutput(workFlowContext, nodeConfig, inputs);
		}

		// 普通模式
		return processNormalOutput(nodeConfig, inputs, workFlowContext);
	}

	/**
	 * 普通输出模式
	 */
	private NodeProcessResult processNormalOutput(LlmNodeConfig nodeConfig, Map<String, Object> inputs,
			WorkFlowContext workFlowContext) {

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

		List<ChatMessage> chatMessages = buildChatMessages(systemPrompt, messages, nodeConfig.getMemory(), inputs,
				workFlowContext);

		ChatRequest chatRequest = ChatRequest.builder().messages(chatMessages).parameters(parameters).build();

		ChatResponse chatResponse = chatModel.chat(chatRequest);

		List<NodeOutputVariable> nodeOutputVariables = List.of(new NodeOutputVariable(
				WorkflowNode.NodeOutputConstants.OUTPUT, "string", chatResponse.aiMessage().text()));
		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

	/**
	 * 结构化输出模式
	 */
	private NodeProcessResult processWithStructuredOutput(WorkFlowContext workFlowContext, LlmNodeConfig nodeConfig,
			Map<String, Object> inputs) {

		StructuredOutputConfig structuredConfig = nodeConfig.getStructuredOutput();
		JsonSchemaConfig schemaConfig = structuredConfig.getSchema();

		if (schemaConfig == null) {
			throw new ArtException("结构化输出配置错误：schema 不能为空");
		}

		// 获取模型上下文（包含模型信息）
		AiModelRuntimeService modelRuntimeService = SpringUtil.getBean(AiModelRuntimeService.class);
		AiModelRuntimeContext modelContext = modelRuntimeService.resolveContext(null,
				Long.valueOf(nodeConfig.getModel()));

		// 自动选择策略
		StructuredOutputStrategy strategy = StructuredOutputStrategySelector.select(modelContext.getModel());
		log.info("模型 {} 使用结构化输出策略: {}", modelContext.getModel().getName(), strategy);

		// 构建消息
		List<ChatMessage> chatMessages = buildChatMessages(nodeConfig.getSystemPrompt(), nodeConfig.getMessages(),
				nodeConfig.getMemory(), inputs, workFlowContext);

		// 应用策略
		ChatRequest chatRequest = applyStrategy(chatMessages, nodeConfig, schemaConfig, strategy);

		// 调用模型
		AiModelInvokeOptions options = AiModelInvokeOptions.builder()
			.timeout(Duration.ofSeconds(nodeConfig.getTimeout()))
			.build();
		ChatModel chatModel = modelRuntimeService.acquireChatModel(null, Long.valueOf(nodeConfig.getModel()), options);
		ChatResponse chatResponse = chatModel.chat(chatRequest);

		String llmResponse = chatResponse.aiMessage().text();
		log.debug("LLM 原始响应: {}", llmResponse);

		// 提取和验证
		Object structuredData = extractStructuredData(llmResponse, schemaConfig, strategy);

		// 返回结构化数据
		List<NodeOutputVariable> nodeOutputVariables = List
			.of(new NodeOutputVariable(WorkflowNode.NodeOutputConstants.OUTPUT, "object", structuredData));
		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

	/**
	 * 构建聊天消息列表
	 */
	private List<ChatMessage> buildChatMessages(String systemPrompt, List<LlmNodeConfig.Message> messages,
			com.art.ai.service.workflow.domain.node.llm.config.MemoryConfig memoryConfig, Map<String, Object> inputs,
			WorkFlowContext workFlowContext) {

		List<ChatMessage> chatMessages = new ArrayList<>();

		// 1. System Prompt
		if (StringUtils.isNoneBlank(systemPrompt)) {
			SystemMessage systemMessage = SystemMessage.from(VariableRenderUtils.format(systemPrompt, inputs));
			chatMessages.add(systemMessage);
		}

		// 2. 手动插入的消息
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

		// 3. 记忆消息（历史对话）
		if (memoryConfig != null && Boolean.TRUE.equals(memoryConfig.getEnabled())) {
			Long conversationId = extractConversationId(workFlowContext);
			if (conversationId != null) {
				ChatMemoryService memoryService = new ChatMemoryService();
				List<ChatMessage> memoryMessages = memoryService.getHistoryMessages(conversationId, memoryConfig);
				chatMessages.addAll(memoryMessages);
			}
		}

		return chatMessages;
	}

	/**
	 * 应用策略（修改 ChatRequest）
	 */
	private ChatRequest applyStrategy(List<ChatMessage> chatMessages, LlmNodeConfig nodeConfig,
			JsonSchemaConfig schemaConfig, StructuredOutputStrategy strategy) {

		DefaultChatRequestParameters.Builder<?> parametersBuilder = ChatRequestParameters.builder()
			.temperature(nodeConfig.getTemperature())
			.maxOutputTokens(nodeConfig.getMaxTokens());

		switch (strategy) {
			case NATIVE -> {
				// 策略 1：使用原生 JSON Schema
				JsonSchema jsonSchema = JsonSchemaConverter.toLangChainJsonSchema(schemaConfig);
				ResponseFormat responseFormat = ResponseFormat.builder().type(JSON).jsonSchema(jsonSchema).build();
				parametersBuilder.responseFormat(responseFormat);
			}
			case JSON_MODE -> {
				// 策略 2：JSON Mode + Prompt 注入
				String schemaPrompt = JsonSchemaPromptBuilder.buildPrompt(schemaConfig);
				chatMessages = injectSchemaPrompt(chatMessages, schemaPrompt);

				ResponseFormat responseFormat = ResponseFormat.builder().type(JSON).build();
				parametersBuilder.responseFormat(responseFormat);
			}
			case PROMPT -> {
				// 策略 3：纯 Prompt 注入
				String schemaPrompt = JsonSchemaPromptBuilder.buildPrompt(schemaConfig);
				chatMessages = injectSchemaPrompt(chatMessages, schemaPrompt);
			}
		}

		return ChatRequest.builder().messages(chatMessages).parameters(parametersBuilder.build()).build();
	}

	/**
	 * 注入 Schema Prompt 到 System Message
	 */
	private List<ChatMessage> injectSchemaPrompt(List<ChatMessage> messages, String schemaPrompt) {
		List<ChatMessage> result = new ArrayList<>(messages);

		// 查找 SystemMessage
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i) instanceof SystemMessage systemMessage) {
				// 追加 Schema 指令
				String enhancedContent = systemMessage.text() + "\n\n" + schemaPrompt;
				result.set(i, SystemMessage.from(enhancedContent));
				return result;
			}
		}

		// 如果没有 SystemMessage，在开头插入
		result.add(0, SystemMessage.from(schemaPrompt));
		return result;
	}

	/**
	 * 提取结构化数据
	 */
	private Object extractStructuredData(String llmResponse, JsonSchemaConfig schemaConfig,
			StructuredOutputStrategy strategy) {

		// NATIVE 策略：模型保证 100% 符合，直接解析
		if (strategy == StructuredOutputStrategy.NATIVE) {
			try {
				return JsonExtractor.extract(llmResponse, schemaConfig);
			}
			catch (JsonExtractor.JsonExtractionException e) {
				log.error("NATIVE 策略提取 JSON 失败（理论上不应该发生）: {}", e.getMessage());
				throw new ArtException("结构化输出提取失败: " + e.getMessage(), e);
			}
		}

		// JSON_MODE 和 PROMPT 策略：需要提取和验证
		try {
			return JsonExtractor.extract(llmResponse, schemaConfig);
		}
		catch (JsonExtractor.JsonExtractionException e) {
			log.error("结构化输出提取失败，策略: {}, 错误: {}", strategy, e.getMessage());
			throw new ArtException("结构化输出提取失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 检查是否启用结构化输出
	 */
	private boolean isStructuredOutputEnabled(LlmNodeConfig config) {
		StructuredOutputConfig structuredConfig = config.getStructuredOutput();
		return structuredConfig != null && Boolean.TRUE.equals(structuredConfig.getEnabled());
	}

}
