package com.art.ai.service.workflow.domain.node.llm.config;

import lombok.Data;

import java.io.Serializable;

/**
 * JSON Schema 根配置
 *
 * @author fxz
 */
@Data
public class JsonSchemaConfig implements Serializable {

	/**
	 * 根节点定义，可表示任意 JSON Schema 类型
	 */
	private JsonSchemaProperty root;

}
