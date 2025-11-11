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
	private final List<AgentPlanItem> plan = Collections.emptyList();

	private final JsonNode raw;

	/**
	 * 是否需要等待用户输入（仅PLAN模式的final决策需要）
	 */
	private final Boolean requiresUserInput;

	public enum DecisionKind {

		/**
		 * 工具调用
		 */
		TOOL_CALLS,

		/**
		 * 最终响应
		 */
		FINAL,

		/**
		 * 规划
		 */
		PLAN,

		/**
		 * 重新规划
		 */
		REPLAN,

		/**
		 * ERROR
		 */
		ERROR

	}

}
