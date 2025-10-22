package com.art.ai.service.workflow.domain.node.llm.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * JSON Schema 属性定义（支持递归嵌套）
 *
 * @author fxz
 */
@Data
public class JsonSchemaProperty implements Serializable {

	/**
	 * 属性类型：string, integer, number, boolean, array, object
	 */
	private String type;

	/**
	 * 字段描述
	 */
	private String description;

	/**
	 * 枚举值约束（可选）
	 */
	@JsonProperty("enum")
	private List<Object> enumValues;

	/**
	 * 数值最小值约束（可选，用于 integer 和 number 类型）
	 */
	private Number minimum;

	/**
	 * 数值最大值约束（可选，用于 integer 和 number 类型）
	 */
	private Number maximum;

	/**
	 * 数组元素定义（当 type = "array" 时使用）
	 */
	private JsonSchemaProperty items;

	/**
	 * 嵌套对象属性定义（当 type = "object" 时使用）
	 */
	private Map<String, JsonSchemaProperty> properties;

	/**
	 * 嵌套对象必填字段（当 type = "object" 时使用）
	 */
	private List<String> required;

	/**
	 * 是否允许额外字段（当 type = "object" 时使用）
	 */
	private Boolean additionalProperties;

}
