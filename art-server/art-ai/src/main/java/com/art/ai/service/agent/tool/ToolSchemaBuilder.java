package com.art.ai.service.agent.tool;

import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 将工具参数描述转换为 JSON Schema（LangChain4j）
 */
public final class ToolSchemaBuilder {

	private ToolSchemaBuilder() {
	}

	public static JsonObjectSchema fromArguments(List<ToolArgumentDescriptor> arguments) {
		JsonObjectSchema.Builder builder = JsonObjectSchema.builder();
		if (arguments == null || arguments.isEmpty()) {
			return builder.additionalProperties(true).build();
		}

		List<String> required = new ArrayList<>();
		for (ToolArgumentDescriptor descriptor : arguments) {
			if (descriptor == null || StringUtils.isBlank(descriptor.getName())) {
				continue;
			}
			builder.addProperty(descriptor.getName(), mapDescriptor(descriptor));
			if (descriptor.isRequired()) {
				required.add(descriptor.getName());
			}
		}

		if (!required.isEmpty()) {
			builder.required(required);
		}
		builder.additionalProperties(false);
		return builder.build();
	}

	private static JsonSchemaElement mapDescriptor(ToolArgumentDescriptor descriptor) {
		String description = descriptor.getDescription();
		String rawType = descriptor.getType();
		return mapType(rawType, description);
	}

	private static JsonSchemaElement mapType(String rawType, String description) {
		String normalized = normalize(rawType);
		if (normalized.startsWith("array")) {
			String elementType = extractArrayItemType(normalized);
			JsonSchemaElement itemSchema = mapType(elementType, description);
			return JsonArraySchema.builder().description(description).items(itemSchema).build();
		}
		return switch (normalized) {
			case "integer", "long" -> JsonIntegerSchema.builder().description(description).build();
			case "number", "double", "float" -> JsonNumberSchema.builder().description(description).build();
			case "boolean" -> JsonBooleanSchema.builder().description(description).build();
			case "object" -> JsonObjectSchema.builder().description(description).additionalProperties(true).build();
			default -> JsonStringSchema.builder().description(description).build();
		};
	}

	private static String normalize(String rawType) {
		if (StringUtils.isBlank(rawType)) {
			return "string";
		}
		return rawType.trim().toLowerCase(Locale.ROOT);
	}

	private static String extractArrayItemType(String type) {
		int start = type.indexOf('<');
		int end = type.lastIndexOf('>');
		if (start >= 0 && end > start) {
			return type.substring(start + 1, end);
		}
		if (type.contains("array<")) {
			return "string";
		}
		return "string";
	}

}
