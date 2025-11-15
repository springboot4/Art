package com.art.ai.service.agent.runtime.strategy.plan;

import com.art.ai.service.agent.runtime.*;
import com.art.ai.service.agent.runtime.AgentDecision;
import com.art.ai.service.agent.runtime.AgentDecisionSchemaProvider;
import com.art.ai.service.agent.runtime.AgentPlanCacheService;
import com.art.ai.service.agent.runtime.AgentPlanStatus;
import com.art.ai.service.agent.runtime.AgentResponseRoute;
import com.art.ai.service.agent.runtime.AgentRunResult;
import com.art.ai.service.agent.runtime.AgentRuntimeException;
import com.art.ai.service.agent.runtime.AgentStep;
import com.art.ai.service.agent.runtime.AgentToolCall;
import com.art.ai.service.agent.runtime.strategy.AgentStrategy;
import com.art.ai.service.agent.runtime.strategy.AgentStrategyContext;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.agent.tool.*;
import com.art.ai.service.agent.tool.AgentTool;
import com.art.ai.service.agent.tool.AgentToolContext;
import com.art.ai.service.agent.tool.AgentToolException;
import com.art.ai.service.agent.tool.AgentToolRequest;
import com.art.ai.service.agent.tool.AgentToolResult;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.request.*;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.TokenUsage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;

/**
 * @author fxz
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PlanExecuteAgentStrategy implements AgentStrategy {

	private final PlanPromptBuilder promptBuilder;

	private final AgentPlanCacheService planCacheService;

	@Override
	public AgentRunResult execute(AgentStrategyContext context) throws AgentToolException {
		PlanRuntimeState state = new PlanRuntimeState(context.getRunId(), context.getAgent().getId(), context.getSpec(),
				context.getUserInput(), context.getConversationId(), context.getMemory(), context.getVariablePool(),
				Instant.now());

		planCacheService.load(context.getRunId(), context.getAgent().getId()).ifPresent(snapshot -> {
			state.restorePlan(snapshot);
			if (state.isPlanEstablished()) {
				if (state.getPlanStatus() == AgentPlanStatus.WAITING_USER
						&& StringUtils.isNotBlank(state.getUserInput())) {
					state.clearWaitingUser();
				}
				if (state.getPlanStatus() == null) {
					state.setPlanStatus(AgentPlanStatus.ACTIVE);
				}
			}
		});

		AgentSpec.Budgets budgets = context.getSpec().getBudgets();
		TokenUsage lastUsage = null;
		String finalMessage;
		String finishReason;

		try {
			while (true) {
				checkBudgets(state, budgets);

				List<ChatMessage> messages = promptBuilder.buildPrompt(state, context.getToolDefinitions(),
						context.getResponseRoute());
				ChatRequestParameters parameters = buildParameters(context, !state.isPlanEstablished());
				ChatRequest chatRequest = ChatRequest.builder().messages(messages).parameters(parameters).build();

				ChatResponse chatResponse = context.getChatModel().chat(chatRequest);
				lastUsage = chatResponse.tokenUsage();
				AgentDecision decision = interpretDecision(chatResponse, context, !state.isPlanEstablished());

				switch (decision.getKind()) {
					case PLAN -> {
						context.notifyProgress("plan", Map.of("steps", decision.getPlan().stream().map(item -> {
							Map<String, Object> stepMap = new HashMap<>();
							stepMap.put("step", item.getStep());
							stepMap.put("goal", item.getGoal());
							if (StringUtils.isNotBlank(item.getTool())) {
								stepMap.put("tool", item.getTool());
							}
							return stepMap;
						}).toList()));

						state.recordPlan(decision.getPlan());
						state.addStep(AgentStep.builder()
							.index(state.getStepCounter() + 1)
							.decisionKind(decision.getKind())
							.summary("生成计划: " + decision.getPlan().size() + " 步")
							.elapsedMs(Duration.between(state.getStartTime(), Instant.now()).toMillis())
							.build());

						if (state.isPlanEstablished()) {
							state.setPlanStatus(AgentPlanStatus.ACTIVE);
							persistPlanCheckpoint(state);
						}
						continue;
					}
					case TOOL_CALLS -> {
						for (AgentToolCall call : decision.getToolCalls()) {
							context.notifyProgress("tool_start",
									Map.of("name", call.getName(), "label", getToolLabel(call.getName(), context)));
						}

						AgentStep step = executeTools(state, decision, context);
						state.addStep(step);

						for (AgentToolCall call : step.getToolCalls()) {
							Map<String, Object> endData = new HashMap<>();
							endData.put("name", call.getName());
							endData.put("label", getToolLabel(call.getName(), context));
							endData.put("status", step.getErrorMessage() == null ? "success" : "failed");
							endData.put("elapsed", step.getElapsedMs());
							if (step.getErrorMessage() != null) {
								endData.put("error", step.getErrorMessage());
							}
							context.notifyProgress("tool_end", endData);
						}

						if (step.getErrorMessage() == null) {
							state.autoAdvancePlan(step.getToolCalls());
						}
						else {
							state.markCurrentStepFailed(step.getErrorMessage());
						}
						persistPlanCheckpoint(state);
						continue;
					}
					case FINAL -> {
						finalMessage = StringUtils.defaultIfBlank(decision.getMessage(), "");
						finishReason = "final";

						if (Boolean.TRUE.equals(decision.getRequiresUserInput())) {
							state.markWaitingForUser(finalMessage);
							persistPlanCheckpoint(state);
						}
						else if (state.getActivePlanIndex() >= state.unmodifiablePlan().size()) {
							state.setPlanStatus(AgentPlanStatus.COMPLETED);
						}
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
			log.warn("Plan-Execute Agent运行中止: {}", ex.getMessage());
			finalMessage = ex.getMessage();
			finishReason = "halt";
		}

		flushPlanCache(state, context.getRunId());

		return AgentRunResult.builder()
			.runId(context.getRunId())
			.output(StringUtils.defaultString(finalMessage))
			.steps(state.unmodifiableSteps())
			.finishReason(finishReason)
			.promptTokens(lastUsage != null ? Long.valueOf(lastUsage.inputTokenCount()) : null)
			.completionTokens(lastUsage != null ? Long.valueOf(lastUsage.outputTokenCount()) : null)
			.totalTokens(lastUsage != null ? Long.valueOf(lastUsage.totalTokenCount()) : null)
			.build();
	}

	@Override
	public AgentSpec.StrategyType getStrategyType() {
		return AgentSpec.StrategyType.PLAN_EXECUTE;
	}

	private ChatRequestParameters buildParameters(AgentStrategyContext context, boolean expectPlan) {
		DefaultChatRequestParameters.Builder builder = ChatRequestParameters.builder()
			.temperature(context.getSpec().getTemperature())
			.maxOutputTokens(context.getSpec().getMaxTokens());

		switch (context.getResponseRoute()) {
			case FUNCTION_CALL -> {
				if (context.getFunctionToolSpecs() != null && !context.getFunctionToolSpecs().isEmpty()) {
					builder.toolSpecifications(context.getFunctionToolSpecs());
					// 强制模型调用函数，防止随意输出文本导致解析失败
					builder.toolChoice(ToolChoice.REQUIRED);
				}
			}
			case STRUCTURED -> builder.responseFormat(ResponseFormat.builder()
				.type(JSON)
				.jsonSchema(AgentDecisionSchemaProvider.schema(true, expectPlan))
				.build());
			case JSON_MODE -> builder.responseFormat(ResponseFormat.builder().type(JSON).build());
			case PROMPT_JSON -> {
			}
		}

		return builder.build();
	}

	private AgentDecision interpretDecision(ChatResponse chatResponse, AgentStrategyContext context, boolean expectPlan)
			throws AgentToolException, AgentRuntimeException {
		if (context.getResponseRoute() == AgentResponseRoute.FUNCTION_CALL) {
			return context.getFunctionCallInterpreter().interpretPlanFunctionCall(chatResponse, expectPlan);
		}
		return context.getDecisionParser().parse(chatResponse.aiMessage().text());
	}

	private AgentStep executeTools(PlanRuntimeState state, AgentDecision decision, AgentStrategyContext context)
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
			AgentTool tool = context.getEnabledTools().get(call.getName());
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

				// 应用参数绑定
				context.getArgumentBinder()
					.applyArgumentBindings(state.getSpec().bindingsFor(call.getName()), state.getVariablePool(),
							preparedArgs);

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
			.observation(observation)
			.elapsedMs((System.nanoTime() - start) / 1_000_000)
			.errorMessage(errorMessage)
			.summary(errorMessage != null ? errorMessage : "工具调用完成")
			.build();
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

	private void checkBudgets(PlanRuntimeState state, AgentSpec.Budgets budgets) throws AgentRuntimeException {
		if (budgets.getMaxSteps() != null && state.getStepCounter() >= budgets.getMaxSteps()) {
			throw new AgentRuntimeException("已达到最大推理步数");
		}
		if (budgets.getMaxToolCalls() != null && state.getToolCallCounter() >= budgets.getMaxToolCalls()) {
			throw new AgentRuntimeException("已达到最大工具调用次数");
		}
	}

	private void persistPlanCheckpoint(PlanRuntimeState state) {
		if (state == null || !state.isPlanEstablished()) {
			return;
		}
		AgentPlanStatus status = state.getPlanStatus() != null ? state.getPlanStatus() : AgentPlanStatus.ACTIVE;
		if (status == AgentPlanStatus.WAITING_USER) {
			planCacheService.markWaiting(state);
		}
		else {
			planCacheService.persist(state, status);
		}
	}

	private void flushPlanCache(PlanRuntimeState state, String runId) {
		if (state == null) {
			return;
		}
		if (state.isPlanEstablished()) {
			AgentPlanStatus status = state.getPlanStatus() != null ? state.getPlanStatus() : AgentPlanStatus.ACTIVE;
			switch (status) {
				case WAITING_USER -> planCacheService.markWaiting(state);
				case COMPLETED -> planCacheService.clear(runId);
				default -> planCacheService.persist(state, status);
			}
		}
		else if (state.getPlanStatus() == AgentPlanStatus.COMPLETED) {
			planCacheService.clear(runId);
		}
	}

	/**
	 * 获取工具的显示名称
	 */
	private String getToolLabel(String toolName, AgentStrategyContext context) {
		if (context.getToolDefinitions() == null) {
			return toolName;
		}

		return context.getToolDefinitions()
			.stream()
			.filter(def -> toolName.equals(def.getName()))
			.map(def -> StringUtils.isNotBlank(def.getDescription()) ? def.getDescription() : toolName)
			.findFirst()
			.orElse(toolName);
	}

}
