package com.art.ai.service.workflow.domain.node.llm;

import com.art.ai.service.workflow.domain.node.llm.config.JsonSchemaConfig;
import com.art.ai.service.workflow.domain.node.llm.converter.JsonSchemaConverter;
import com.art.ai.service.workflow.domain.node.llm.converter.JsonSchemaPromptBuilder;
import com.art.ai.service.workflow.domain.node.llm.extractor.JsonExtractor;
import com.art.ai.service.workflow.domain.node.llm.extractor.JsonExtractor.JsonExtractionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonSchemaSupportTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	void shouldConvertNestedSchema() {
		// 场景：构造包含数组与严格子对象的嵌套 Schema
		// 期望 JSON Schema 结构（根为 object）：
		// {
		// "name": string,
		// "scores": number[],
		// "metadata": { "source": string }
		// }
		JsonSchemaConfig config = buildSampleObjectConfig();

		// 操作：转换为 LangChain4j 的 JsonSchema，预期根对象标记 required=["name","scores"],
		// additionalProperties=false
		JsonSchema jsonSchema = JsonSchemaConverter.toLangChainJsonSchema(config);
		assertEquals("response_schema", jsonSchema.name());

		JsonSchemaElement rootElement = jsonSchema.rootElement();
		assertTrue(rootElement instanceof JsonObjectSchema, "Root element should be JsonObjectSchema");

		JsonObjectSchema objectSchema = (JsonObjectSchema) rootElement;
		assertEquals(List.of("name", "scores"), objectSchema.required());
		assertEquals(Boolean.FALSE, objectSchema.additionalProperties());
		Map<String, JsonSchemaElement> properties = objectSchema.properties();
		assertEquals(3, properties.size());

		assertTrue(properties.get("scores") instanceof JsonArraySchema);
		JsonArraySchema scoresSchema = (JsonArraySchema) properties.get("scores");
		assertTrue(scoresSchema.items() instanceof JsonNumberSchema, "scores array must enforce number items");

		assertTrue(properties.get("metadata") instanceof JsonObjectSchema);
		JsonObjectSchema metadataSchema = (JsonObjectSchema) properties.get("metadata");
		assertEquals(Boolean.FALSE, metadataSchema.additionalProperties());
		assertTrue(metadataSchema.properties().containsKey("source"));
	}

	@Test
	void shouldBuildPromptWithStructureHints() {
		// 场景：使用与上个用例一致的 Schema，希望 Prompt 明确字段结构和示例
		// 期望 Prompt 中出现的 JSON 示例：
		// {
		// "name": "example",
		// "scores": [0.0],
		// "metadata": { "source": "example" }
		// }
		JsonSchemaConfig config = buildSampleObjectConfig();

		// 操作：生成 Prompt 指令，预期包含 name 字段、scores 数组以及 metadata 对象的描述和示例 JSON
		String prompt = JsonSchemaPromptBuilder.buildPrompt(config);
		assertTrue(prompt.startsWith("You MUST respond with a valid JSON value matching this schema"));
		assertTrue(prompt.contains("\"name\": string (required)"));
		assertTrue(prompt.contains("\"scores\""));
		assertTrue(prompt.contains("\"metadata\""));
		assertTrue(prompt.contains("Example:"));
		assertTrue(prompt.contains("\"name\": \"example\""));
		assertTrue(prompt.contains("- Do NOT add fields not declared in the schema"));
	}

	@Test
	void shouldExtractAndValidateStructuredJson() throws JsonExtractionException {
		// 场景：提供符合 Schema 的合法响应
		// 实际响应 JSON：
		// {
		// "name": "张三",
		// "scores": [95.5, 88.0],
		// "metadata": { "source": "manual" }
		// }
		JsonSchemaConfig config = buildSampleObjectConfig();

		String json = "{\"name\":\"张三\",\"scores\":[95.5,88.0],\"metadata\":{\"source\":\"manual\"}}";
		Object result = JsonExtractor.extract(json, config);
		assertInstanceOf(Map.class, result);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) result;
		assertEquals("张三", data.get("name"));
		assertEquals(List.of(95.5, 88.0), data.get("scores"));
		assertInstanceOf(Map.class, data.get("metadata"));
	}

	@Test
	void shouldRejectUnexpectedFieldWhenAdditionalPropertiesDisabled() {
		// 场景：响应包含 Schema 禁止的多余字段
		// 实际响应 JSON：
		// {
		// "name": "张三",
		// "scores": [95.5],
		// "metadata": { "source": "manual" },
		// "extra": 1
		// }
		JsonSchemaConfig config = buildSampleObjectConfig();
		String json = "{\"name\":\"张三\",\"scores\":[95.5],\"metadata\":{\"source\":\"manual\"},\"extra\":1}";

		JsonExtractionException ex = assertThrows(JsonExtractionException.class,
				() -> JsonExtractor.extract(json, config));
		assertTrue(ex.getMessage().contains("不允许的额外字段"));
	}

	@Test
	void shouldRejectInvalidArrayElementType() {
		// 场景：数组元素出现错误类型
		// 实际响应 JSON：
		// {
		// "name": "张三",
		// "scores": [95.5, "oops"],
		// "metadata": { "source": "manual" }
		// }
		JsonSchemaConfig config = buildSampleObjectConfig();
		String json = "{\"name\":\"张三\",\"scores\":[95.5,\"oops\"],\"metadata\":{\"source\":\"manual\"}}";

		JsonExtractionException ex = assertThrows(JsonExtractionException.class,
				() -> JsonExtractor.extract(json, config));
		assertTrue(ex.getMessage().contains("期望数值"));
	}

	@Test
	void shouldHandleArrayRootSchemas() throws JsonExtractionException {
		// 场景：根节点即为对象数组
		// 实际响应 JSON：
		// [
		// { "city": "北京", "population": 21893095 }
		// ]
		JsonSchemaConfig config = buildArrayRootConfig();

		String json = "[{\"city\":\"北京\",\"population\":21893095}]";
		Object result = JsonExtractor.extract(json, config);
		assertInstanceOf(List.class, result);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) result;
		assertEquals(1, list.size());
		assertEquals("北京", list.get(0).get("city"));
	}

	// 构建含字符串、数组、嵌套对象且启用严格校验的对象根 Schema
	// 目标 Schema JSON：
	// {
	// "type": "object",
	// "properties": {
	// "name": { "type": "string" },
	// "scores": { "type": "array", "items": { "type": "number" } },
	// "metadata": {
	// "type": "object",
	// "properties": { "source": { "type": "string" } },
	// "additionalProperties": false
	// }
	// },
	// "required": ["name", "scores"],
	// "additionalProperties": false
	// }
	private JsonSchemaConfig buildSampleObjectConfig() {
		String schemaJson = """
				{
				  "root": {
				    "type": "object",
				    "description": "结果结构",
				    "properties": {
				      "name": {
				        "type": "string",
				        "description": "用户姓名"
				      },
				      "scores": {
				        "type": "array",
				        "description": "得分数组",
				        "items": { "type": "number" }
				      },
				      "metadata": {
				        "type": "object",
				        "description": "元信息",
				        "properties": {
				          "source": { "type": "string" }
				        },
				        "additionalProperties": false
				      }
				    },
				    "required": ["name", "scores"],
				    "additionalProperties": false
				  }
				}
				""";
		return readConfig(schemaJson);
	}

	// 构建数组根 Schema，验证非 object 根节点也能正确处理
	// 目标 Schema JSON：
	// {
	// "type": "array",
	// "items": {
	// "type": "object",
	// "properties": {
	// "city": { "type": "string" },
	// "population": { "type": "integer", "minimum": 0 }
	// },
	// "required": ["city"],
	// "additionalProperties": false
	// }
	// }
	private JsonSchemaConfig buildArrayRootConfig() {
		String schemaJson = """
				{
				  "root": {
				    "type": "array",
				    "description": "城市列表",
				    "items": {
				      "type": "object",
				      "properties": {
				        "city": { "type": "string" },
				        "population": { "type": "integer", "minimum": 0 }
				      },
				      "required": ["city"],
				      "additionalProperties": false
				    }
				  }
				}
				""";
		return readConfig(schemaJson);
	}

	/**
	 * 通过 JSON 字符串构造 JsonSchemaConfig，模拟前端真实入参
	 */
	private JsonSchemaConfig readConfig(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, JsonSchemaConfig.class);
		}
		catch (IOException e) {
			throw new IllegalStateException("解析 Schema JSON 失败", e);
		}
	}

}
