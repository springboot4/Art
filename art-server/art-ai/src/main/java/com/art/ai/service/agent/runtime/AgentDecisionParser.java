package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.runtime.dto.AgentDecisionPayload;
import com.art.ai.service.agent.runtime.dto.AgentFinalDecisionPayload;
import com.art.ai.service.agent.runtime.dto.AgentFinalFunctionPayload;
import com.art.ai.service.agent.runtime.dto.AgentPlanDecisionPayload;
import com.art.ai.service.agent.runtime.dto.AgentPlanFunctionPayload;
import com.art.ai.service.agent.runtime.dto.AgentPlanItemPayload;
import com.art.ai.service.agent.runtime.dto.AgentToolCallPayload;
import com.art.ai.service.agent.runtime.dto.AgentToolDecisionPayload;
import com.art.ai.service.agent.tool.AgentToolException;
import com.art.core.common.util.CollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agent 决策解析器
 *
 * @author fxz
 * @since 2025-11-01
 */
@Component
@RequiredArgsConstructor
public class AgentDecisionParser {

	private final ObjectMapper objectMapper;

	public AgentDecision parse(String content) throws AgentToolException {
		if (StringUtils.isBlank(content)) {
			throw new AgentToolException("模型输出为空，无法解析决策");
		}

		try {
			AgentDecisionPayload payload = objectMapper.readValue(content, AgentDecisionPayload.class);

			List<AgentToolCall> toolCalls = payload instanceof AgentToolDecisionPayload toolPayload
					? toToolCalls(toolPayload.getToolCalls()) : List.of();

			List<AgentPlanItem> plan = payload instanceof AgentPlanDecisionPayload planPayload
					? toPlan(planPayload.getPlan()) : List.of();

			String message = payload instanceof AgentFinalDecisionPayload finalPayload ? finalPayload.getMessage()
					: null;

			Boolean requiresUserInput = payload instanceof AgentFinalDecisionPayload finalPayload
					? finalPayload.getRequiresUserInput() : null;

			return AgentDecision.builder()
				.kind(payload.mapKind())
				.toolCalls(toolCalls)
				.message(message)
				.plan(plan)
				.requiresUserInput(requiresUserInput)
				.raw(objectMapper.valueToTree(payload))
				.build();
		}
		catch (Exception ex) {
			throw new AgentToolException("解析模型决策失败: " + ex.getMessage(), ex);
		}
	}

	public AgentDecision parseControlDecision(AgentControlFunction function, JsonNode payload)
			throws AgentToolException {
		JsonNode safePayload = payload == null ? JsonNodeFactory.instance.objectNode() : payload;
		return switch (function) {
			case PLAN -> buildPlanDecision(convertPayload(safePayload, AgentPlanFunctionPayload.class));
			case FINAL -> buildFinalDecision(convertPayload(safePayload, AgentFinalFunctionPayload.class));
		};
	}

	public JsonNode parseArguments(String rawArgs) throws AgentToolException {
		try {
			if (StringUtils.isBlank(rawArgs)) {
				return JsonNodeFactory.instance.objectNode();
			}
			return objectMapper.readTree(rawArgs);
		}
		catch (Exception ex) {
			throw new AgentToolException("工具参数不是合法 JSON: " + ex.getMessage(), ex);
		}
	}

	private AgentDecision buildPlanDecision(AgentPlanFunctionPayload payload) {
		List<AgentPlanItem> plan = toPlan(payload != null ? payload.getItems() : Collections.emptyList());
		return AgentDecision.builder()
			.kind(AgentDecision.DecisionKind.PLAN)
			.plan(plan)
			.raw(objectMapper.valueToTree(payload))
			.build();
	}

	private AgentDecision buildFinalDecision(AgentFinalFunctionPayload payload) {
		AgentFinalFunctionPayload safe = payload == null ? new AgentFinalFunctionPayload() : payload;
		return AgentDecision.builder()
			.kind(AgentDecision.DecisionKind.FINAL)
			.message(safe.getMessage())
			.requiresUserInput(safe.getRequiresUserInput())
			.raw(objectMapper.valueToTree(payload))
			.build();
	}

	private List<AgentToolCall> toToolCalls(List<AgentToolCallPayload> payloads) {
		if (payloads == null || payloads.isEmpty()) {
			return List.of();
		}

		List<AgentToolCall> calls = new ArrayList<>();
		for (AgentToolCallPayload payload : payloads) {
			if (payload == null || StringUtils.isBlank(payload.getName())) {
				continue;
			}
			calls.add(AgentToolCall.builder().name(payload.getName()).arguments(payload.getArgs()).build());
		}
		return calls;
	}

	private List<AgentPlanItem> toPlan(List<AgentPlanItemPayload> payloads) {
		if (CollectionUtil.isEmpty(payloads)) {
			return List.of();
		}

		List<AgentPlanItem> plan = new ArrayList<>();
		int fallback = 1;
		for (AgentPlanItemPayload payload : payloads) {
			if (payload == null) {
				continue;
			}

			int step = payload.getStep() != null ? payload.getStep() : fallback++;
			String stepId = StringUtils.defaultIfBlank(payload.getStepId(), "step-" + step);
			AgentPlanItemType type = mapPlanType(payload.getType());
			AgentPlanItem item = AgentPlanItem.builder()
				.step(step)
				.stepId(stepId)
				.goal(payload.getGoal())
				.tool(payload.getTool())
				.requiresUser(payload.getRequiresUser())
				.type(type)
				.build();
			plan.add(item);
		}
		return plan;
	}

	private <T> T convertPayload(JsonNode node, Class<T> type) throws AgentToolException {
		try {
			return objectMapper.treeToValue(node, type);
		}
		catch (JsonProcessingException ex) {
			throw new AgentToolException("控制函数返回结构异常: " + ex.getOriginalMessage(), ex);
		}
	}

	private AgentPlanItemType mapPlanType(String type) {
		if (StringUtils.isBlank(type)) {
			return AgentPlanItemType.UNKNOWN;
		}
		try {
			return AgentPlanItemType.valueOf(type.trim().toUpperCase());
		}
		catch (IllegalArgumentException ex) {
			return AgentPlanItemType.UNKNOWN;
		}
	}

}
