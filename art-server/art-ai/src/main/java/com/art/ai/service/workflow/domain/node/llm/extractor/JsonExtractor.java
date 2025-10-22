package com.art.ai.service.workflow.domain.node.llm.extractor;

import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaConfig;
import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON 提取器
 * <p>
 * 负责从 LLM 响应中提取 JSON 并验证
 *
 * @author fxz
 */
@Slf4j
@UtilityClass
public class JsonExtractor {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final Pattern JSON_BLOCK_PATTERN = Pattern.compile("```json\\s*([\\s\\S]*?)\\s*```",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern JSON_OBJECT_PATTERN = Pattern.compile("\\{[\\s\\S]*\\}");

	private static final Pattern JSON_ARRAY_PATTERN = Pattern.compile("\\[[\\s\\S]*\\]");

	/**
	 * 从 LLM 响应中提取并验证 JSON
	 * @param response LLM 原始响应
	 * @param schemaConfig JSON Schema 配置
	 * @return 提取的 JSON 对象（可能是 Map、List 或原始类型）
	 * @throws JsonExtractionException 提取或验证失败时抛出
	 */
	public Object extract(String response, JsonSchemaConfig schemaConfig) throws JsonExtractionException {
		if (response == null || response.isBlank()) {
			throw new JsonExtractionException("LLM 响应为空");
		}

		JsonSchemaProperty rootSchema = schemaConfig != null ? schemaConfig.getRoot() : null;
		if (rootSchema == null || !StringUtils.hasText(rootSchema.getType())) {
			throw new JsonExtractionException("结构化输出配置错误：缺少根类型定义");
		}
		Exception loggedException = null;
		// Level 1: 直接解析
		try {
			Object result = parseJson(response);
			validate(result, rootSchema);
			return result;
		}
		catch (Exception e) {
			log.debug("Level 1 解析失败: {}", e.getMessage());
		}

		// Level 2: 清理后解析（去除 markdown 代码块）
		try {
			String cleaned = cleanMarkdown(response);
			Object result = parseJson(cleaned);
			validate(result, rootSchema);
			return result;
		}
		catch (Exception e) {
			log.debug("Level 2 解析失败: {}", e.getMessage());
		}

		// Level 3: 正则提取
		try {
			String extracted = extractJsonByRegex(response);
			Object result = parseJson(extracted);
			validate(result, rootSchema);
			return result;
		}
		catch (Exception e) {
			log.debug("Level 3 解析失败: {}", e.getMessage());
			loggedException = e;
		}

		// 所有方法都失败，抛出异常
		log.error("JSON 提取失败，原始响应: {}", response);
		throw new JsonExtractionException(loggedException.getMessage());
	}

	/**
	 * 解析 JSON 字符串
	 */
	private Object parseJson(String jsonString) throws JsonProcessingException {
		return MAPPER.readValue(jsonString, Object.class);
	}

	/**
	 * 清理 markdown 代码块标记
	 */
	private String cleanMarkdown(String text) {
		// 去除 ```json ... ``` 包裹
		Matcher matcher = JSON_BLOCK_PATTERN.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}

		// 去除 ``` ... ``` 包裹
		text = text.replaceAll("```[\\s\\S]*?```", "").trim();

		// 去除前后空白
		return text.trim();
	}

	/**
	 * 使用正则表达式提取 JSON
	 */
	private String extractJsonByRegex(String text) throws JsonExtractionException {
		Matcher objectMatcher = JSON_OBJECT_PATTERN.matcher(text);
		if (objectMatcher.find()) {
			return objectMatcher.group();
		}

		Matcher arrayMatcher = JSON_ARRAY_PATTERN.matcher(text);
		if (arrayMatcher.find()) {
			return arrayMatcher.group();
		}

		throw new JsonExtractionException("未找到 JSON 结构");
	}

	/**
	 * 验证 JSON 是否符合 Schema
	 */
	/**
	 * 入口校验方法：收集错误信息后统一抛出
	 */
	private void validate(Object data, JsonSchemaProperty schema) throws JsonExtractionException {
		List<String> errors = new ArrayList<>();
		validateRecursive(data, schema, "$", errors);
		if (!errors.isEmpty()) {
			throw new JsonExtractionException("JSON Schema 验证失败: " + String.join(", ", errors));
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * 深度优先校验：根据节点类型递归验证结构和值
	 */
	private void validateRecursive(Object data, JsonSchemaProperty schema, String path, List<String> errors) {
		if (schema == null || !StringUtils.hasText(schema.getType())) {
			return;
		}

		switch (schema.getType().toLowerCase()) {
			case "object" -> {
				if (!(data instanceof Map<?, ?> map)) {
					errors.add(path + " 期望对象，但得到 " + typeName(data));
					return;
				}
				Map<String, Object> objectData = (Map<String, Object>) map;
				Map<String, JsonSchemaProperty> properties = schema.getProperties();
				List<String> required = schema.getRequired();
				if (!CollectionUtils.isEmpty(required)) {
					required.forEach(field -> {
						if (!objectData.containsKey(field)) {
							errors.add(path + "." + field + " 缺少必填字段");
							return;
						}
						Object value = objectData.get(field);
						if (value == null && !isNullable(properties, field)) {
							errors.add(path + "." + field + " 不能为空");
						}
					});
				}

				if (!CollectionUtils.isEmpty(properties)) {
					properties.forEach((key, childSchema) -> {
						Object value = objectData.get(key);
						if (value == null) {
							return;
						}
						validateRecursive(value, childSchema, path + "." + key, errors);
					});
				}

				if (Boolean.FALSE.equals(schema.getAdditionalProperties()) && !CollectionUtils.isEmpty(properties)) {
					for (String key : objectData.keySet()) {
						if (!properties.containsKey(key)) {
							errors.add(path + " 不允许的额外字段: " + key);
						}
					}
				}
			}
			case "array" -> {
				if (!(data instanceof List<?> list)) {
					errors.add(path + " 期望数组，但得到 " + typeName(data));
					return;
				}
				JsonSchemaProperty itemSchema = schema.getItems();
				if (itemSchema != null) {
					for (int i = 0; i < list.size(); i++) {
						Object element = list.get(i);
						if (element == null) {
							if (!"null".equalsIgnoreCase(itemSchema.getType())) {
								errors.add(path + "[" + i + "] 期望 " + itemSchema.getType() + "，但得到 null");
							}
							continue;
						}
						validateRecursive(element, itemSchema, path + "[" + i + "]", errors);
					}
				}
			}
			case "string" -> {
				if (!(data instanceof String value)) {
					errors.add(path + " 期望字符串，但得到 " + typeName(data));
					return;
				}
				if (!CollectionUtils.isEmpty(schema.getEnumValues())) {
					boolean match = schema.getEnumValues()
						.stream()
						.anyMatch(enumValue -> String.valueOf(enumValue).equals(value));
					if (!match) {
						errors.add(path + " 的取值不在允许的枚举范围内");
					}
				}
			}
			case "integer" -> {
				if (!(data instanceof Number number)) {
					errors.add(path + " 期望整数，但得到 " + typeName(data));
					return;
				}
				double doubleValue = number.doubleValue();
				if (doubleValue % 1 != 0) {
					errors.add(path + " 期望整数，但得到小数");
				}
				checkNumberRange(doubleValue, schema, path, errors);
			}
			case "number" -> {
				if (!(data instanceof Number number)) {
					errors.add(path + " 期望数值，但得到 " + typeName(data));
					return;
				}
				checkNumberRange(number.doubleValue(), schema, path, errors);
			}
			case "boolean" -> {
				if (!(data instanceof Boolean)) {
					errors.add(path + " 期望布尔值，但得到 " + typeName(data));
				}
			}
			case "null" -> {
				if (data != null) {
					errors.add(path + " 期望 null，但得到 " + typeName(data));
				}
			}
			default -> {
				// 未识别的类型暂不校验，避免阻塞流程
			}
		}
	}

	/**
	 * 校验数值型的最小值/最大值约束
	 */
	private void checkNumberRange(double value, JsonSchemaProperty schema, String path, List<String> errors) {
		Number minimum = schema.getMinimum();
		Number maximum = schema.getMaximum();
		if (minimum != null && value < minimum.doubleValue()) {
			errors.add(path + " 数值小于最小值 " + minimum);
		}
		if (maximum != null && value > maximum.doubleValue()) {
			errors.add(path + " 数值大于最大值 " + maximum);
		}
	}

	/**
	 * 判断字段类型是否显式声明为 null，用于放行空值
	 */
	private boolean isNullable(Map<String, JsonSchemaProperty> properties, String field) {
		if (CollectionUtils.isEmpty(properties)) {
			return false;
		}
		JsonSchemaProperty property = properties.get(field);
		return property != null && "null".equalsIgnoreCase(property.getType());
	}

	/**
	 * 辅助方法：打印友好的类型名
	 */
	private String typeName(Object value) {
		return value == null ? "null" : value.getClass().getSimpleName();
	}

	/**
	 * JSON 提取异常
	 */
	public static class JsonExtractionException extends Exception {

		public JsonExtractionException(String message) {
			super(message);
		}

		public JsonExtractionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
