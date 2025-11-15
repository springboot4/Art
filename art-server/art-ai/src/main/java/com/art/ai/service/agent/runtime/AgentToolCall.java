package com.art.ai.service.agent.runtime;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

/**
 * Agent 工具调用描述
 *
 * @author fxz
 */
@Data
@Builder(toBuilder = true)
public class AgentToolCall {

	private String name;

	private JsonNode arguments;

}
