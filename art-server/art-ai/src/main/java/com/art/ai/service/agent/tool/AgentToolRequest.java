package com.art.ai.service.agent.tool;

import com.art.ai.service.agent.spec.AgentSpec;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

/**
 * 工具执行请求
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentToolRequest {

	private final AgentSpec agentSpec;

	private final AgentToolContext context;

	private final JsonNode arguments;

}
