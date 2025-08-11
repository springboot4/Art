package com.art.ai.service.workflow.variable;

import lombok.Getter;

/**
 * @author fxz
 */

@Getter
public enum VariableType {

	ENVIRONMENT("env", "环境变量"), SYSTEM("sys", "系统变量"), CONVERSATION("conversation", "会话变量"),
	USER_INPUT("input", "用户输入"), NODE_OUTPUT("output", "节点出参");

	@com.fasterxml.jackson.annotation.JsonValue
	private final String type;

	private final String description;

	VariableType(String type, String description) {
		this.type = type;
		this.description = description;
	}

	@com.fasterxml.jackson.annotation.JsonCreator
	public static VariableType of(String type) {
		for (VariableType variableType : values()) {
			if (variableType.getType().equals(type)) {
				return variableType;
			}
		}
		throw new IllegalArgumentException("未知的变量类型: " + type);
	}

}