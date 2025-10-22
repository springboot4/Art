package com.art.ai.service.workflow.domain.node.llm.converter;

import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaConfig;
import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaProperty;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * JSON Schema 转 Prompt 构建器
 *
 * @author fxz
 */
@UtilityClass
public class JsonSchemaPromptBuilder {

	/**
	 * 将 JSON Schema 转换为 Prompt 指令
	 */
	public String buildPrompt(JsonSchemaConfig config) {
		JsonSchemaProperty root = config != null ? config.getRoot() : null;
		if (root == null || !StringUtils.hasText(root.getType())) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("You MUST respond with a valid JSON value matching this schema:\n\n");
		sb.append(renderStructure(root, 0)).append("\n\n");

		sb.append("Example:\n");
		sb.append(renderExample(root, 0)).append("\n\n");

		sb.append("Rules:\n");
		sb.append("- Output ONLY the JSON value\n");
		sb.append("- Do NOT include markdown formatting (like ```json)\n");
		if (isStrictObject(root)) {
			sb.append("- Do NOT add fields not declared in the schema\n");
		}
		List<String> requiredFields = topLevelRequired(root);
		if (!requiredFields.isEmpty()) {
			sb.append("- Ensure all required fields are present: ")
				.append(String.join(", ", requiredFields))
				.append("\n");
		}

		return sb.toString();
	}

	/**
	 * 判断对象节点是否禁止出现额外字段
	 */
	private boolean isStrictObject(JsonSchemaProperty property) {
		return "object".equalsIgnoreCase(property.getType())
				&& Boolean.FALSE.equals(property.getAdditionalProperties());
	}

	/**
	 * 仅针对根对象返回必填字段列表
	 */
	private List<String> topLevelRequired(JsonSchemaProperty property) {
		if (!"object".equalsIgnoreCase(property.getType())) {
			return Collections.emptyList();
		}
		List<String> required = property.getRequired();
		return CollectionUtils.isEmpty(required) ? Collections.emptyList() : required;
	}

	/**
	 * 渲染结构说明，用于告诉模型每个字段的类型
	 */
	private String renderStructure(JsonSchemaProperty property, int indent) {
		if (property == null || !StringUtils.hasText(property.getType())) {
			return indent(indent) + "any";
		}

		return switch (property.getType().toLowerCase()) {
			case "object" -> renderObjectStructure(property, indent);
			case "array" -> renderArrayStructure(property, indent);
			case "string" -> indent(indent) + describeString(property);
			case "integer" -> indent(indent) + describeNumeric("integer", property);
			case "number" -> indent(indent) + describeNumeric("number", property);
			case "boolean" -> indent(indent) + "boolean";
			default -> indent(indent) + property.getType().toLowerCase();
		};
	}

	/**
	 * 渲染对象类型的字段说明
	 */
	private String renderObjectStructure(JsonSchemaProperty property, int indent) {
		String indentStr = indent(indent);
		StringBuilder sb = new StringBuilder();
		sb.append(indentStr).append("{\n");
		List<String> lines = new ArrayList<>();
		Map<String, JsonSchemaProperty> properties = property.getProperties();
		if (!CollectionUtils.isEmpty(properties)) {
			properties.forEach((name, child) -> {
				StringBuilder line = new StringBuilder();
				line.append(indent(indent + 1))
					.append("\"")
					.append(name)
					.append("\": ")
					.append(cleanLeadingIndent(renderStructure(child, indent + 1)));
				line.append(requiredSuffix(property.getRequired(), name));
				if (StringUtils.hasText(child.getDescription())) {
					line.append(" // ").append(child.getDescription());
				}
				lines.add(line.toString());
			});
		}
		sb.append(String.join(",\n", lines));
		if (!lines.isEmpty()) {
			sb.append("\n");
		}
		sb.append(indentStr).append("}");
		return sb.toString();
	}

	/**
	 * 渲染数组类型说明，必要时换行展示子结构
	 */
	private String renderArrayStructure(JsonSchemaProperty property, int indent) {
		String indentStr = indent(indent);
		StringBuilder sb = new StringBuilder();
		sb.append(indentStr).append("[");
		JsonSchemaProperty itemProperty = property.getItems();
		if (itemProperty != null) {
			String rendered = cleanLeadingIndent(renderStructure(itemProperty, indent + 1));
			if (rendered.contains("\n")) {
				sb.append("\n")
					.append(indentMultiline(rendered, indent + 1))
					.append("\n")
					.append(indentStr)
					.append("]");
				return sb.toString();
			}
			sb.append(" ").append(rendered.trim()).append(" ]");
			return sb.toString();
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 渲染示例 JSON，帮助模型对齐返回格式
	 */
	private String renderExample(JsonSchemaProperty property, int indent) {
		if (property == null || !StringUtils.hasText(property.getType())) {
			return indent(indent) + "null";
		}

		return switch (property.getType().toLowerCase()) {
			case "object" -> renderObjectExample(property, indent);
			case "array" -> renderArrayExample(property, indent);
			case "string" -> indent(indent) + exampleString(property);
			case "integer" -> indent(indent) + exampleInteger(property);
			case "number" -> indent(indent) + exampleNumber(property);
			case "boolean" -> indent(indent) + "true";
			default -> indent(indent) + "null";
		};
	}

	/**
	 * 渲染对象类型的示例值
	 */
	private String renderObjectExample(JsonSchemaProperty property, int indent) {
		String indentStr = indent(indent);
		StringBuilder sb = new StringBuilder();
		sb.append(indentStr).append("{\n");
		List<String> lines = new ArrayList<>();
		Map<String, JsonSchemaProperty> properties = property.getProperties();
		if (!CollectionUtils.isEmpty(properties)) {
			properties.forEach((name, child) -> {
				String value = renderExample(child, indent + 1).trim();
				lines.add(indent(indent + 1) + "\"" + name + "\": " + value);
			});
		}
		sb.append(String.join(",\n", lines));
		if (!lines.isEmpty()) {
			sb.append("\n");
		}
		sb.append(indentStr).append("}");
		return sb.toString();
	}

	/**
	 * 渲染数组类型的示例值
	 */
	private String renderArrayExample(JsonSchemaProperty property, int indent) {
		String indentStr = indent(indent);
		StringBuilder sb = new StringBuilder();
		sb.append(indentStr).append("[");
		JsonSchemaProperty itemProperty = property.getItems();
		if (itemProperty != null) {
			String rendered = cleanLeadingIndent(renderExample(itemProperty, indent + 1));
			if (rendered.contains("\n")) {
				sb.append("\n")
					.append(indentMultiline(rendered, indent + 1))
					.append("\n")
					.append(indentStr)
					.append("]");
				return sb.toString();
			}
			sb.append(" ").append(rendered.trim()).append(" ]");
			return sb.toString();
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 生成字符串类型的描述，优先列举枚举值
	 */
	private String describeString(JsonSchemaProperty property) {
		if (!CollectionUtils.isEmpty(property.getEnumValues())) {
			List<String> values = property.getEnumValues().stream().map(value -> "\"" + value + "\"").toList();
			return String.join(" or ", values);
		}
		return "string";
	}

	/**
	 * 数值类型描述，拼接最小值/最大值提示
	 */
	private String describeNumeric(String type, JsonSchemaProperty property) {
		if (property.getMinimum() != null && property.getMaximum() != null) {
			return type + " between " + property.getMinimum() + "-" + property.getMaximum();
		}
		if (property.getMinimum() != null) {
			return type + " >= " + property.getMinimum();
		}
		if (property.getMaximum() != null) {
			return type + " <= " + property.getMaximum();
		}
		return type;
	}

	private String exampleString(JsonSchemaProperty property) {
		if (!CollectionUtils.isEmpty(property.getEnumValues())) {
			return "\"" + property.getEnumValues().get(0) + "\"";
		}
		return "\"example\"";
	}

	private String exampleInteger(JsonSchemaProperty property) {
		if (property.getMinimum() != null) {
			return String.valueOf(property.getMinimum().intValue());
		}
		return "0";
	}

	private String exampleNumber(JsonSchemaProperty property) {
		if (property.getMinimum() != null) {
			return String.valueOf(property.getMinimum().doubleValue());
		}
		return "0.0";
	}

	private String requiredSuffix(List<String> required, String name) {
		return required != null && required.contains(name) ? " (required)" : " (optional)";
	}

	private String indent(int level) {
		return "  ".repeat(Math.max(level, 0));
	}

	private String indentMultiline(String value, int level) {
		String normalized = cleanLeadingIndent(value);
		String prefix = indent(level);
		return prefix + normalized.replace("\n", "\n" + prefix);
	}

	private String cleanLeadingIndent(String value) {
		return value == null ? "" : value.stripLeading();
	}

}
