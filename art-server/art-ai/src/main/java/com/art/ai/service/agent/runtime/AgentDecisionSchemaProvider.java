package com.art.ai.service.agent.runtime;

import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaConfig;
import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaProperty;
import com.art.ai.service.workflow.domain.node.llm.converter.JsonSchemaConverter;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Agent 决策结构的 JSON Schema 提供器 根据agent策略类型和阶段动态生成schema
 *
 * @author fxz
 */
@UtilityClass
public final class AgentDecisionSchemaProvider {

	private AgentDecisionSchemaProvider() {
	}

	/**
	 * 获取决策schema
	 * @param isPlanMode 是否是PLAN模式
	 * @param expectPlan 是否期望生成计划
	 * @return JSON Schema
	 */
	public static JsonSchema schema(boolean isPlanMode, boolean expectPlan) {
		JsonSchemaConfig config = buildConfig(isPlanMode, expectPlan);
		return JsonSchemaConverter.toLangChainJsonSchema(config);
	}

	private static JsonSchemaConfig buildConfig(boolean isPlanMode, boolean expectPlan) {
		JsonSchemaProperty root = new JsonSchemaProperty();
		root.setType("object");
		root.setRequired(List.of("kind"));
		root.setAdditionalProperties(false);

		Map<String, JsonSchemaProperty> properties = new HashMap<>();
		properties.put("kind", buildKindProperty(isPlanMode, expectPlan));
		properties.put("message", buildStringProperty("面向用户的文本"));

		if (expectPlan) {
			// 期望生成计划
			properties.put("plan", buildPlanProperty());
		}
		else {
			// 执行阶段
			properties.put("toolCalls", buildToolCallsProperty());
			if (isPlanMode) {
				// PLAN模式的final需要requiresUserInput
				properties.put("requiresUserInput", buildBooleanProperty("是否需要用户输入"));
			}
		}

		root.setProperties(properties);

		JsonSchemaConfig config = new JsonSchemaConfig();
		config.setRoot(root);
		return config;
	}

	private static JsonSchemaProperty buildKindProperty(boolean isPlanMode, boolean expectPlan) {
		JsonSchemaProperty property = new JsonSchemaProperty();
		property.setType("string");

		if (expectPlan) {
			property.setDescription("决策类型，当前阶段只能是: plan");
			property.setEnumValues(List.of("plan"));
		}
		else if (isPlanMode) {
			property.setDescription("决策类型: tool_calls | final");
			property.setEnumValues(List.of("tool_calls", "final"));
		}
		else {
			// REACT模式
			property.setDescription("决策类型: tool_calls | final");
			property.setEnumValues(List.of("tool_calls", "final"));
		}

		return property;
	}

	private static JsonSchemaProperty buildStringProperty(String description) {
		JsonSchemaProperty property = new JsonSchemaProperty();
		property.setType("string");
		property.setDescription(description);
		return property;
	}

	private static JsonSchemaProperty buildBooleanProperty(String description) {
		JsonSchemaProperty property = new JsonSchemaProperty();
		property.setType("boolean");
		property.setDescription(description);
		return property;
	}

	private static JsonSchemaProperty buildPlanProperty() {
		JsonSchemaProperty plan = new JsonSchemaProperty();
		plan.setType("array");
		plan.setDescription("执行计划的步骤列表");

		JsonSchemaProperty planItem = new JsonSchemaProperty();
		planItem.setType("object");
		planItem.setProperties(Map.of("step", buildSimpleProperty("integer", "步骤序号"), "goal",
				buildStringProperty("该步骤要达成的目标"), "tool", buildStringProperty("可选：建议使用的工具名称")));
		planItem.setRequired(List.of("step", "goal"));
		planItem.setAdditionalProperties(false);

		plan.setItems(planItem);
		return plan;
	}

	private static JsonSchemaProperty buildToolCallsProperty() {
		JsonSchemaProperty calls = new JsonSchemaProperty();
		calls.setType("array");
		calls.setDescription("工具调用列表");

		JsonSchemaProperty callItem = new JsonSchemaProperty();
		callItem.setType("object");
		callItem.setProperties(Map.of("name", buildStringProperty("工具名称"), "args", buildObjectProperty("工具参数")));
		callItem.setRequired(List.of("name"));
		callItem.setAdditionalProperties(false);
		calls.setItems(callItem);
		return calls;
	}

	private static JsonSchemaProperty buildObjectProperty(String description) {
		JsonSchemaProperty property = new JsonSchemaProperty();
		property.setType("object");
		property.setDescription(description);
		property.setAdditionalProperties(true);
		return property;
	}

	private static JsonSchemaProperty buildSimpleProperty(String type, String description) {
		JsonSchemaProperty property = new JsonSchemaProperty();
		property.setType(type);
		property.setDescription(description);
		return property;
	}

}
