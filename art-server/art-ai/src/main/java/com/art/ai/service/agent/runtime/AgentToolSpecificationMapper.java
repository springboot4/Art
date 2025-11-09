package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.tool.AgentToolDefinition;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;

/**
 * 工具定义 -> LangChain4j ToolSpecification
 */
public final class AgentToolSpecificationMapper {

	private AgentToolSpecificationMapper() {
	}

	public static ToolSpecification toSpecification(AgentToolDefinition definition) {
		JsonObjectSchema schema = definition.parametersSchemaOrDefault();
		return ToolSpecification.builder()
			.name(definition.getName())
			.description(definition.getDescription())
			.parameters(schema)
			.build();
	}

}
