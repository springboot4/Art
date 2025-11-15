package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.tool.AgentToolDefinition;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import lombok.experimental.UtilityClass;

/**
 * 工具定义 -> LangChain4j ToolSpecification
 *
 * @author fxz
 */
@UtilityClass
public final class AgentToolSpecificationMapper {

	public static ToolSpecification toSpecification(AgentToolDefinition definition) {
		JsonObjectSchema schema = definition.parametersSchemaOrDefault();
		return ToolSpecification.builder()
			.name(definition.getName())
			.description(definition.getDescription())
			.parameters(schema)
			.build();
	}

}
