package com.art.ai.service.agent.runtime;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Agent 运行步骤
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentStep {

	private final int index;

	private final AgentDecision.DecisionKind decisionKind;

	@Builder.Default
	private final List<AgentToolCall> toolCalls = Collections.emptyList();

	@Builder.Default
	private final Map<String, Object> observation = Collections.emptyMap();

	private final long elapsedMs;

	private final String thought;

	private final String errorMessage;

	private final String summary;

}
