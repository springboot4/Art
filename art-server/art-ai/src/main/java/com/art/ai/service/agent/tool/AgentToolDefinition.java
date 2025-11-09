package com.art.ai.service.agent.tool;

import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Agent 工具定义
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentToolDefinition {

	private String name;

	private String description;

	private Duration timeout;

	@Builder.Default
	private List<ToolArgumentDescriptor> arguments = Collections.emptyList();

	/**
	 * tool function 的 JSON Schema 定义
	 */
	private JsonObjectSchema parametersSchema;

	public JsonObjectSchema parametersSchemaOrDefault() {
		if (parametersSchema != null) {
			return parametersSchema;
		}
		return ToolSchemaBuilder.fromArguments(arguments);
	}

}
