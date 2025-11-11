package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.tool.AgentToolException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Function Call响应解释器 负责将模型的Function Call响应解析为AgentDecision
 *
 * @author fxz
 */
@Component
@RequiredArgsConstructor
public class AgentFunctionCallInterpreter {

	private final AgentDecisionParser decisionParser;

	/**
	 * 解析Function Call响应
	 */
	public AgentDecision interpretReactFunctionCall(ChatResponse response)
			throws AgentToolException, AgentRuntimeException {
		AiMessage aiMessage = response.aiMessage();
		List<ToolExecutionRequest> requests = aiMessage.toolExecutionRequests();

		if (aiMessage.hasToolExecutionRequests() && requests != null && !requests.isEmpty()) {
			boolean containsControl = requests.stream()
				.anyMatch(req -> AgentControlFunction.fromName(req.name()).isPresent());
			boolean containsExternal = requests.stream()
				.anyMatch(req -> AgentControlFunction.fromName(req.name()).isEmpty());

			if (containsControl && containsExternal) {
				throw new AgentRuntimeException("控制函数与外部工具不能在同一轮混用");
			}

			if (containsControl) {
				if (requests.size() != 1) {
					throw new AgentRuntimeException("同一轮仅允许调用一个控制函数");
				}
				ToolExecutionRequest request = requests.get(0);
				AgentControlFunction function = AgentControlFunction.fromName(request.name())
					.orElseThrow(() -> new AgentRuntimeException("未知控制函数: " + request.name()));

				if (function != AgentControlFunction.FINAL) {
					throw new AgentRuntimeException("REACT模式不支持控制函数: " + function.getName());
				}

				JsonNode args = decisionParser.parseArguments(request.arguments());
				return decisionParser.parseControlDecision(function, args);
			}

			List<AgentToolCall> calls = new ArrayList<>();
			for (ToolExecutionRequest request : requests) {
				JsonNode args = decisionParser.parseArguments(request.arguments());
				calls.add(AgentToolCall.builder().name(request.name()).arguments(args).build());
			}
			return AgentDecision.builder().kind(AgentDecision.DecisionKind.TOOL_CALLS).toolCalls(calls).build();
		}

		String fallback = aiMessage.text();
		if (StringUtils.isBlank(fallback)) {
			throw new AgentRuntimeException("模型未返回可解析的 FunctionCall 结果");
		}
		return decisionParser.parse(fallback);
	}

	/**
	 * 解析Function Call响应
	 */
	public AgentDecision interpretPlanFunctionCall(ChatResponse response, boolean expectPlan)
			throws AgentToolException, AgentRuntimeException {
		AiMessage aiMessage = response.aiMessage();
		List<ToolExecutionRequest> requests = aiMessage.toolExecutionRequests();

		if (aiMessage.hasToolExecutionRequests() && requests != null && !requests.isEmpty()) {
			boolean containsControl = requests.stream()
				.anyMatch(req -> AgentControlFunction.fromName(req.name()).isPresent());
			boolean containsExternal = requests.stream()
				.anyMatch(req -> AgentControlFunction.fromName(req.name()).isEmpty());

			if (containsControl && containsExternal) {
				throw new AgentRuntimeException("控制函数与外部工具不能在同一轮混用");
			}

			if (containsControl) {
				if (requests.size() != 1) {
					throw new AgentRuntimeException("同一轮仅允许调用一个控制函数");
				}
				ToolExecutionRequest request = requests.get(0);
				AgentControlFunction function = AgentControlFunction.fromName(request.name())
					.orElseThrow(() -> new AgentRuntimeException("未知控制函数: " + request.name()));

				if (expectPlan && function != AgentControlFunction.PLAN) {
					throw new AgentRuntimeException("计划阶段仅允许 agent_plan");
				}

				JsonNode args = decisionParser.parseArguments(request.arguments());
				return decisionParser.parseControlDecision(function, args);
			}

			if (expectPlan) {
				throw new AgentRuntimeException("计划阶段仅允许 agent_plan");
			}

			List<AgentToolCall> calls = new ArrayList<>();
			for (ToolExecutionRequest request : requests) {
				if (AgentControlFunction.fromName(request.name()).isPresent()) {
					throw new AgentRuntimeException("控制函数与外部工具不能在同一轮混用");
				}
				JsonNode args = decisionParser.parseArguments(request.arguments());
				calls.add(AgentToolCall.builder().name(request.name()).arguments(args).build());
			}
			return AgentDecision.builder().kind(AgentDecision.DecisionKind.TOOL_CALLS).toolCalls(calls).build();
		}

		String fallback = aiMessage.text();
		if (StringUtils.isBlank(fallback)) {
			throw new AgentRuntimeException("模型未返回可解析的 FunctionCall 结果");
		}
		return decisionParser.parse(fallback);
	}

}
