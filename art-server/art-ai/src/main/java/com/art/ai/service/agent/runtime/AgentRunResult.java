package com.art.ai.service.agent.runtime;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Agent 运行结果
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentRunResult {

	private final String runId;

	private final String output;

	@Builder.Default
	private final List<AgentStep> steps = Collections.emptyList();

	private final String finishReason;

	private final Long promptTokens;

	private final Long completionTokens;

	private final Long totalTokens;

}
