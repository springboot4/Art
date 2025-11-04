package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.tool.AgentToolException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
			JsonNode root = objectMapper.readTree(content);
			String kindValue = root.path("kind").asText("tool_calls").toLowerCase();
			AgentDecision.DecisionKind kind = mapKind(kindValue);

			List<AgentToolCall> toolCalls = parseToolCalls(root.path("tool_calls"));
			List<String> citations = parseCitations(root.path("citations"));
			List<AgentPlanItem> plan = parsePlan(root.path("plan"));

			return AgentDecision.builder()
				.kind(kind)
				.toolCalls(toolCalls)
				.message(root.path("message").asText(null))
				.citations(citations)
				.plan(plan)
				.thought(root.path("thought").asText(null))
				.stopReason(root.path("stopReason").asText(null))
				.raw(root)
				.build();
		}
		catch (Exception ex) {
			throw new AgentToolException("解析模型决策失败: " + ex.getMessage(), ex);
		}
	}

	private AgentDecision.DecisionKind mapKind(String kind) {
		return switch (kind) {
			case "tool_calls" -> AgentDecision.DecisionKind.TOOL_CALLS;
			case "final" -> AgentDecision.DecisionKind.FINAL;
			case "plan" -> AgentDecision.DecisionKind.PLAN;
			case "ask_user" -> AgentDecision.DecisionKind.ASK_USER;
			case "halt" -> AgentDecision.DecisionKind.HALT;
			case "error" -> AgentDecision.DecisionKind.ERROR;
			default -> AgentDecision.DecisionKind.TOOL_CALLS;
		};
	}

	private List<AgentToolCall> parseToolCalls(JsonNode node) {
		List<AgentToolCall> toolCalls = new ArrayList<>();
		if (node == null || !node.isArray()) {
			return toolCalls;
		}

		ArrayNode arrayNode = (ArrayNode) node;
		arrayNode.forEach(item -> {
			String name = item.path("name").asText(null);
			JsonNode args = item.path("args");
			if (StringUtils.isNotBlank(name)) {
				toolCalls.add(AgentToolCall.builder().name(name).arguments(args).build());
			}
		});
		return toolCalls;
	}

	private List<String> parseCitations(JsonNode node) {
		List<String> citations = new ArrayList<>();
		if (node == null || !node.isArray()) {
			return citations;
		}
		node.forEach(item -> citations.add(item.asText()));
		return citations;
	}

	private List<AgentPlanItem> parsePlan(JsonNode node) {
		List<AgentPlanItem> plan = new ArrayList<>();
		if (node == null || !node.isArray()) {
			return plan;
		}
		int stepIndex = 1;
		for (JsonNode item : node) {
			String goal = item.path("goal").asText(null);
			String tool = item.path("tool").asText(null);
			plan.add(AgentPlanItem.builder().step(item.path("step").asInt(stepIndex++)).goal(goal).tool(tool).build());
		}
		return plan;
	}

}
