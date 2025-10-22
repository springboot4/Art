package com.art.ai.service.workflow.domain.node.llm.converter;

import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaConfig;
import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaProperty;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JSON Schema 转换器：将我们的清晰结构转换为 LangChain4j 的 JsonSchema
 *
 * @author fxz
 */
@UtilityClass
public class JsonSchemaConverter {

	/**
	 * 转换为 LangChain4j 的 JsonSchema
	 */
	public JsonSchema toLangChainJsonSchema(JsonSchemaConfig config) {
		if (config == null) {
			throw new IllegalArgumentException("JsonSchemaConfig cannot be null");
		}

		JsonSchemaProperty rootProperty = config.getRoot();
		if (rootProperty == null) {
			throw new IllegalArgumentException("JsonSchemaConfig root cannot be null");
		}

		JsonSchemaElement rootElement = buildSchemaElement(rootProperty);

		return JsonSchema.builder().name("response_schema").rootElement(rootElement).build();
	}

	/**
	 * 递归将自定义节点转换为 LangChain4j 的 JsonSchemaElement
	 */
	private JsonSchemaElement buildSchemaElement(JsonSchemaProperty property) {
		if (property == null || !StringUtils.hasText(property.getType())) {
			return JsonObjectSchema.builder().build();
		}

		return switch (property.getType().toLowerCase()) {
			case "object" -> buildObjectSchema(property);
			case "array" -> buildArraySchema(property);
			case "string" -> buildStringSchema(property);
			case "integer" -> buildIntegerSchema(property);
			case "number" -> buildNumberSchema(property);
			case "boolean" -> buildBooleanSchema(property);
			default -> buildFallbackSchema(property);
		};
	}

	/**
	 * 构造字符串类型 Schema，可选择挂载枚举约束
	 */
	private JsonSchemaElement buildStringSchema(JsonSchemaProperty property) {
		if (!CollectionUtils.isEmpty(property.getEnumValues())) {
			JsonEnumSchema.Builder builder = JsonEnumSchema.builder();
			if (StringUtils.hasText(property.getDescription())) {
				builder.description(property.getDescription());
			}
			List<String> enumValues = property.getEnumValues()
				.stream()
				.map(String::valueOf)
				.collect(Collectors.toList());
			builder.enumValues(enumValues);
			return builder.build();
		}

		JsonStringSchema.Builder builder = JsonStringSchema.builder();
		if (StringUtils.hasText(property.getDescription())) {
			builder.description(property.getDescription());
		}
		return builder.build();
	}

	/**
	 * 构造整数类型 Schema，仅透传描述信息
	 */
	private JsonSchemaElement buildIntegerSchema(JsonSchemaProperty property) {
		JsonIntegerSchema.Builder builder = JsonIntegerSchema.builder();
		if (StringUtils.hasText(property.getDescription())) {
			builder.description(property.getDescription());
		}
		return builder.build();
	}

	/**
	 * 构造数值类型 Schema，仅透传描述信息
	 */
	private JsonSchemaElement buildNumberSchema(JsonSchemaProperty property) {
		JsonNumberSchema.Builder builder = JsonNumberSchema.builder();
		if (StringUtils.hasText(property.getDescription())) {
			builder.description(property.getDescription());
		}
		return builder.build();
	}

	/**
	 * 构造布尔类型 Schema，仅透传描述信息
	 */
	private JsonSchemaElement buildBooleanSchema(JsonSchemaProperty property) {
		JsonBooleanSchema.Builder builder = JsonBooleanSchema.builder();
		if (StringUtils.hasText(property.getDescription())) {
			builder.description(property.getDescription());
		}
		return builder.build();
	}

	/**
	 * 构造数组类型 Schema，并递归生成元素定义
	 */
	private JsonArraySchema buildArraySchema(JsonSchemaProperty property) {
		JsonArraySchema.Builder builder = JsonArraySchema.builder();
		if (StringUtils.hasText(property.getDescription())) {
			builder.description(property.getDescription());
		}
		if (property.getItems() != null) {
			builder.items(buildSchemaElement(property.getItems()));
		}
		return builder.build();
	}

	/**
	 * 构造对象类型 Schema，同时处理子字段、必填项及额外字段策略
	 */
	private JsonObjectSchema buildObjectSchema(JsonSchemaProperty property) {
		JsonObjectSchema.Builder builder = JsonObjectSchema.builder();
		if (StringUtils.hasText(property.getDescription())) {
			builder.description(property.getDescription());
		}

		Map<String, JsonSchemaProperty> properties = property.getProperties();
		if (!CollectionUtils.isEmpty(properties)) {
			properties.forEach((name, childProperty) -> builder.addProperty(name, buildSchemaElement(childProperty)));
		}

		List<String> required = property.getRequired();
		if (!CollectionUtils.isEmpty(required)) {
			builder.required(required);
		}

		if (property.getAdditionalProperties() != null) {
			builder.additionalProperties(property.getAdditionalProperties());
		}

		return builder.build();
	}

	private JsonSchemaElement buildFallbackSchema(JsonSchemaProperty property) {
		// 默认退化为字符串，以最大限度兼容未知类型（注意：若上游能严格校验 type，建议改为抛异常）
		return buildStringSchema(property);
	}

}
