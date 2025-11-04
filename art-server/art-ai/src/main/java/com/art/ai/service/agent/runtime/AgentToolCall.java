package com.art.ai.service.agent.runtime;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

/**
 * Agent 工具调用描述
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentToolCall {

	private final String name;

	private final JsonNode arguments;

}
