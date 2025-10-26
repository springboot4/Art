package com.art.ai.service.workflow.variable;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fxz
 */
@UtilityClass
public class VariablePoolManager {

	/**
	 * 创建新的变量池
	 */
	public VariablePool createPool(Map<SystemVariableKey, Object> systemVars, Map<String, Object> envVars,
			Map<String, Object> conversationVars, Map<String, Object> userInputs) {
		return VariablePool.create(systemVars, envVars, conversationVars, userInputs);
	}

	/**
	 * 更新会话变量(变量赋值节点使用)
	 */
	public void updateConversationVariable(String variableName, Object value, ConversationVariableWriteMode mode,
			VariablePool pool) {
		if (pool != null) {
			if (mode == ConversationVariableWriteMode.MERGE) {
				mergeConversationValue(variableName, value, pool);
			}
			else {
				var selector = VariableSelector.conversation(variableName);
				pool.update(selector, value);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void mergeConversationValue(String variableName, Object value, VariablePool pool) {
		var selector = VariableSelector.conversation(variableName);
		Object current = pool.getValue(selector, Map.class).orElse(null);
		if (!(current instanceof Map<?, ?> currentMap)) {
			throw new IllegalStateException("当前会话变量不是对象，无法执行 merge: " + variableName);
		}
		if (!(value instanceof Map<?, ?> incoming)) {
			throw new IllegalStateException("merge 模式要求写入对象: " + variableName);
		}
		ConcurrentHashMap<String, Object> merged = new ConcurrentHashMap<>();
		currentMap.forEach((k, v) -> merged.put(String.valueOf(k), v));
		incoming.forEach((k, v) -> merged.put(String.valueOf(k), v));
		pool.update(selector, merged);
	}

	/**
	 * 更新节点输出变量
	 */
	public void updateNodeOutputVariable(String nodeId, String variableName, Object value, VariablePool pool) {
		if (pool != null) {
			var selector = VariableSelector.of(nodeId, variableName);
			pool.add(selector, value);
		}
	}

}