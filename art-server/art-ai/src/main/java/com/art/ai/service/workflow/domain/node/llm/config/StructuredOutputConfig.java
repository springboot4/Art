package com.art.ai.service.workflow.domain.node.llm.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 结构化输出配置
 *
 * @author fxz
 */
@Data
public class StructuredOutputConfig implements Serializable {

	/**
	 * 是否启用结构化输出
	 */
	private Boolean enabled = false;

	/**
	 * JSON Schema 定义
	 */
	private JsonSchemaConfig schema;

}
