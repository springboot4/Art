package com.art.ai.service.workflow.variable;

import lombok.experimental.UtilityClass;

import java.util.Map;

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
	public void updateConversationVariable(String variableName, Object value, VariablePool pool) {
		if (pool != null) {
			var selector = VariableSelector.conversation(variableName);
			pool.update(selector, value);
		}
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