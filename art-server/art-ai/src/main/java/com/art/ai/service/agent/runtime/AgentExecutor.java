package com.art.ai.service.agent.runtime;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.agent.spec.AgentSpecJsonHelper;
import com.art.ai.service.agent.tool.AgentTool;
import com.art.ai.service.agent.tool.AgentToolContext;
import com.art.ai.service.agent.tool.AgentToolDefinition;
import com.art.ai.service.agent.tool.AgentToolException;
import com.art.ai.service.agent.tool.AgentToolRegistry;
import com.art.ai.service.agent.tool.AgentToolRequest;
import com.art.ai.service.agent.tool.AgentToolResult;
import com.art.ai.service.agent.tool.ToolArgumentBinding;
import com.art.ai.service.conversation.variable.ConversationVariableService;
import com.art.ai.service.message.MessageService;
import com.art.ai.service.model.runtime.AiModelRuntimeService;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariableSelector;
import com.art.ai.service.workflow.variable.VariableValue;
import com.art.core.common.exception.ArtException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.TokenUsage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;

/**
 * Agent 执行器
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AgentExecutor {

	private final AiModelRuntimeService modelRuntimeService;

	private final MessageService messageService;

	private final AgentPromptBuilder promptBuilder;

	private final AgentDecisionParser decisionParser;

	private final AgentToolRegistry toolRegistry;

	private final ObjectMapper objectMapper;

	private final ConversationVariableService conversationVariableService;

	public AgentRunResult run(AiAgentDTO agent, AgentRunRequest request)
			throws AgentRuntimeException, AgentToolException {
		AgentSpec spec = AgentSpecJsonHelper.parse(agent.getSpecJson());
		String runId = UUID.randomUUID().toString();

		List<AiMessageDTO> memory = loadMemory(spec, request.getConversationId());

		Map<String, Object> variables = request.getVariables() == null ? Collections.emptyMap()
				: request.getVariables();

		Map<String, Object> conversationVars = loadConversationVariables(request.getConversationId(), agent.getAppId());

		AgentRuntimeState state = new AgentRuntimeState(runId, spec, agent, request.getInput(),
				request.getConversationId(), memory, variables, conversationVars, Instant.now());

		Map<String, AgentTool> enabledTools = resolveEnabledTools(spec.getTools());
		List<AgentToolDefinition> toolDefinitions = enabledTools.values().stream().map(AgentTool::definition).toList();

		ChatModel chatModel = acquireChatModel(spec);
		AgentSpec.Budgets budgets = spec.getBudgets();
		List<String> citations = new ArrayList<>();
		TokenUsage lastUsage = null;
		String finalMessage;
		String finishReason;
		try {
			while (true) {
				checkBudgets(state, budgets);
				boolean expectPlan = spec.getStrategy().getType() == AgentSpec.StrategyType.PLAN_EXECUTE
						&& !state.isPlanEstablished();

				List<ChatMessage> messages = promptBuilder.buildPrompt(state, toolDefinitions, expectPlan);
				ChatRequestParameters parameters = ChatRequestParameters.builder()
					.temperature(spec.getTemperature())
					.maxOutputTokens(spec.getMaxTokens())
					.responseFormat(ResponseFormat.builder().type(JSON).build())
					.build();
				ChatRequest chatRequest = ChatRequest.builder().messages(messages).parameters(parameters).build();

				ChatResponse chatResponse = chatModel.chat(chatRequest);
				lastUsage = chatResponse.tokenUsage();
				String content = chatResponse.aiMessage().text();
				AgentDecision decision = decisionParser.parse(content);

				if (decision.getCitations() != null && !decision.getCitations().isEmpty()) {
					citations = decision.getCitations();
				}

				switch (decision.getKind()) {
					case PLAN -> {
						state.recordPlan(decision.getPlan());
						state.addStep(AgentStep.builder()
							.index(state.getStepCounter() + 1)
							.decisionKind(decision.getKind())
							.thought(decision.getThought())
							.summary("PLAN -> " + decision.getPlan())
							.elapsedMs(Duration.between(state.getStartTime(), Instant.now()).toMillis())
							.build());
						continue;
					}

					case TOOL_CALLS -> {
						AgentStep step = executeTools(state, decision, enabledTools);
						state.addStep(step);
						continue;
					}

					case ASK_USER, HALT, ERROR -> {
						finalMessage = StringUtils.defaultIfBlank(decision.getMessage(), "无法继续执行，请联系管理员");
						finishReason = decision.getKind().name().toLowerCase();
						break;
					}
					case FINAL -> {
						finalMessage = StringUtils.defaultIfBlank(decision.getMessage(), "");
						finishReason = "final";
						break;
					}
					default -> {
						finalMessage = "无法解析的决策";
						finishReason = decision.getKind().name().toLowerCase();
						break;
					}
				}

				break;
			}
		}
		catch (AgentRuntimeException ex) {
			log.warn("Agent 运行中止: {}", ex.getMessage());
			finalMessage = ex.getMessage();
			finishReason = "halt";
		}

		return AgentRunResult.builder()
			.runId(runId)
			.output(StringUtils.defaultString(finalMessage))
			.citations(citations)
			.steps(state.unmodifiableSteps())
			.finishReason(finishReason)
			.promptTokens(lastUsage != null ? Long.valueOf(lastUsage.inputTokenCount()) : null)
			.completionTokens(lastUsage != null ? Long.valueOf(lastUsage.outputTokenCount()) : null)
			.totalTokens(lastUsage != null ? Long.valueOf(lastUsage.totalTokenCount()) : null)
			.build();
	}

	private AgentStep executeTools(AgentRuntimeState state, AgentDecision decision, Map<String, AgentTool> enabledTools)
			throws AgentRuntimeException {
		List<AgentToolCall> toolCalls = decision.getToolCalls();
		if (toolCalls.isEmpty()) {
			throw new AgentRuntimeException("模型返回 tool_calls 但未提供调用列表");
		}

		List<AgentToolCall> executedCalls = new ArrayList<>();
		Map<String, Object> observation = new HashMap<>();
		long start = System.nanoTime();
		String errorMessage = null;

		for (AgentToolCall call : toolCalls) {
			AgentTool tool = enabledTools.get(call.getName());
			if (tool == null) {
				errorMessage = "未注册的工具: " + call.getName();
				break;
			}
			try {
				Map<String, Object> extras = new HashMap<>();
				if (state.getConversationId() != null) {
					extras.put(SystemVariableKey.CONVERSATION_ID.getKey(), state.getConversationId());
				}

				ObjectNode preparedArgs = ensureArgumentsObject(call);

				applyArgumentBindings(state.getSpec().bindingsFor(call.getName()), state, preparedArgs);

				AgentToolResult result = tool.invoke(AgentToolRequest.builder()
					.agentSpec(state.getSpec())
					.context(AgentToolContext.builder()
						.conversationId(state.getConversationId())
						.runId(state.getRunId())
						.language(state.getSpec().getLanguage())
						.extras(extras)
						.build())
					.arguments(preparedArgs)
					.build());
				observation.put(call.getName(), result.getData());
				executedCalls.add(AgentToolCall.builder().name(call.getName()).arguments(preparedArgs).build());
			}
			catch (AgentToolException ex) {
				errorMessage = ex.getMessage();
				log.error("工具执行失败: {}", call.getName(), ex);
				break;
			}
		}

		return AgentStep.builder()
			.index(state.getStepCounter() + 1)
			.decisionKind(decision.getKind())
			.toolCalls(executedCalls)
			.thought(decision.getThought())
			.observation(observation)
			.elapsedMs((System.nanoTime() - start) / 1_000_000)
			.errorMessage(errorMessage)
			.summary(errorMessage != null ? errorMessage : "调用工具完成")
			.build();
	}

	private void checkBudgets(AgentRuntimeState state, AgentSpec.Budgets budgets) throws AgentRuntimeException {
		if (budgets.getMaxSteps() != null && state.getStepCounter() >= budgets.getMaxSteps()) {
			throw new AgentRuntimeException("已达到最大推理步数");
		}
		if (budgets.getMaxToolCalls() != null && state.getToolCallCounter() >= budgets.getMaxToolCalls()) {
			throw new AgentRuntimeException("已达到最大工具调用次数");
		}
		// todo fxz
		// if (budgets.getMaxTimeMs() != null) {
		// long elapsed = Duration.between(state.getStartTime(),
		// Instant.now()).toMillis();
		// if (elapsed > budgets.getMaxTimeMs()) {
		// throw new AgentRuntimeException("已超出最大运行时长");
		// }
		// }
	}

	private Map<String, AgentTool> resolveEnabledTools(List<String> toolNames) {
		Map<String, AgentTool> result = new HashMap<>();
		if (toolNames == null || toolNames.isEmpty()) {
			return result;
		}

		for (String name : toolNames) {
			AgentTool tool = toolRegistry.resolve(name);
			if (tool == null) {
				throw new ArtException("未注册的工具: " + name);
			}
			result.put(name, tool);
		}

		return result;
	}

	private ObjectNode ensureArgumentsObject(AgentToolCall call) throws AgentRuntimeException {
		JsonNode args = call.getArguments();
		if (args == null || args.isNull()) {
			return JsonNodeFactory.instance.objectNode();
		}
		if (!args.isObject()) {
			throw new AgentRuntimeException("工具 " + call.getName() + " 的 args 必须是 JSON 对象");
		}

		return args.deepCopy();
	}

	private void applyArgumentBindings(List<ToolArgumentBinding> bindings, AgentRuntimeState state,
			ObjectNode argsNode) {
		if (bindings == null || bindings.isEmpty()) {
			return;
		}
		for (ToolArgumentBinding binding : bindings) {
			if (binding == null || StringUtils.isBlank(binding.getField())) {
				continue;
			}

			String field = binding.getField();
			JsonNode existing = argsNode.get(field);
			boolean hasExisting = existing != null && !existing.isNull();
			boolean shouldOverride = binding.isOverride() || !hasExisting;
			if (!shouldOverride) {
				continue;
			}

			JsonNode value = resolveBindingValue(binding, state);
			if (value == null || value.isNull()) {
				continue;
			}

			argsNode.set(field, cloneNode(value));
		}
	}

	private JsonNode resolveBindingValue(ToolArgumentBinding binding, AgentRuntimeState state) {
		if (binding.getSelector() != null) {
			return resolveSelector(binding.getSelector(), state);
		}

		if (binding.getConstant() != null) {
			return objectMapper.valueToTree(binding.getConstant());
		}

		return null;
	}

	private JsonNode resolveSelector(VariableSelector selector, AgentRuntimeState state) {
		if (selector == null) {
			return null;
		}
		var pool = state.getVariablePool();
		if (pool == null) {
			return null;
		}

		return pool.get(selector).map(VariableValue::getValue).map(this::toJsonNode).orElse(null);
	}

	private ChatModel acquireChatModel(AgentSpec spec) {
		AiModelInvokeOptions options = AiModelInvokeOptions.builder()
			.temperature(BigDecimal.valueOf(spec.getTemperature()))
			.maxOutputTokens(spec.getMaxTokens())
			.timeout(Duration.ofSeconds(30))
			.build();
		return modelRuntimeService.acquireChatModel(Long.valueOf(spec.getPlatformId()), Long.valueOf(spec.getModelId()),
				options);
	}

	private List<AiMessageDTO> loadMemory(AgentSpec spec, Long conversationId) {
		if (conversationId == null) {
			return List.of();
		}

		AgentSpec.Memory memory = spec.getMemory();
		if (memory == null || Boolean.FALSE.equals(memory.getEnabled())) {
			return List.of();
		}

		int size = memory.getWindow() != null && memory.getWindow().getSize() != null ? memory.getWindow().getSize()
				: 10;
		return messageService.getRecentMessages(conversationId, size);
	}

	private Map<String, Object> loadConversationVariables(Long conversationId, Long appId) {
		if (conversationId == null) {
			return Collections.emptyMap();
		}

		return conversationVariableService.initialize(conversationId, appId, Collections.emptyMap()).variables();
	}

	private JsonNode cloneNode(JsonNode value) {
		if (value == null) {
			return null;
		}

		return value.isContainerNode() ? value.deepCopy() : value;
	}

	private JsonNode toJsonNode(Object value) {
		if (value == null) {
			return objectMapper.nullNode();
		}
		if (value instanceof JsonNode jsonNode) {
			return jsonNode.deepCopy();
		}
		return objectMapper.valueToTree(value);
	}

}
