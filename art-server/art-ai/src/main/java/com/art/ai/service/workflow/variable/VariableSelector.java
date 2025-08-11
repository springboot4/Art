package com.art.ai.service.workflow.variable;

import java.util.Objects;

/**
 * @author fxz
 * @param nodeId
 * @param key
 */
public record VariableSelector(String nodeId, String key) {

	public VariableSelector {
		Objects.requireNonNull(nodeId, "nodeId不能为空");
		Objects.requireNonNull(key, "key不能为空");
	}

	public static VariableSelector of(VariableType variableType, String nodeId, String key) {
		switch (variableType) {
			case ENVIRONMENT -> {
				return environment(key);
			}
			case SYSTEM -> {
				return new VariableSelector(VariableType.SYSTEM.getType(), key);
			}
			case CONVERSATION -> {
				return conversation(key);
			}
			case USER_INPUT -> {
				return userInput(key);
			}
			case NODE_OUTPUT -> {
				return of(nodeId, key);
			}
			default -> throw new IllegalArgumentException("未知的变量类型: " + variableType);
		}
	}

	public static VariableSelector of(String nodeId, String key) {
		return new VariableSelector(nodeId, key);
	}

	public static VariableSelector system(SystemVariableKey key) {
		return new VariableSelector(VariableType.SYSTEM.getType(), key.getKey());
	}

	public static VariableSelector environment(String key) {
		return new VariableSelector(VariableType.ENVIRONMENT.getType(), key);
	}

	public static VariableSelector conversation(String key) {
		return new VariableSelector(VariableType.CONVERSATION.getType(), key);
	}

	private static VariableSelector userInput(String key) {
		return new VariableSelector(VariableType.USER_INPUT.getType(), key);
	}

	public String getVariableName() {
		return String.join(".", key);
	}

	public int getHashKey() {
		return Objects.hash(key);
	}

}