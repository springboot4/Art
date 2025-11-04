package com.art.ai.service.agent.runtime;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Agent 决策结果
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentDecision {

	private final DecisionKind kind;

	@Builder.Default
	private final List<AgentToolCall> toolCalls = Collections.emptyList();

	private final String message;

	@Builder.Default
	private final List<String> citations = Collections.emptyList();

	@Builder.Default
	private final List<AgentPlanItem> plan = Collections.emptyList();

	private final String thought;

	private final String stopReason;

	private final JsonNode raw;

	public enum DecisionKind {

		TOOL_CALLS, FINAL, PLAN, ASK_USER, HALT, ERROR

	}

}
