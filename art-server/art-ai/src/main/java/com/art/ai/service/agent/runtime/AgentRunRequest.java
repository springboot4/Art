package com.art.ai.service.agent.runtime;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * Agent 运行请求
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentRunRequest {

	private final String runId;

	private final Long agentId;

	private final Long conversationId;

	private final String input;

	@Builder.Default
	private final Map<String, Object> variables = Collections.emptyMap();

	@Builder.Default
	private final Map<String, Object> overrides = Collections.emptyMap();

}
