package com.art.ai.service.workflow.variable;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fxz
 */

@Getter
public enum SystemVariableKey {

	USER_ID("userId"), QUERY("query"), CONVERSATION_ID("sessionId"), WORKFLOW_ID("workflowId"),
	WORKFLOW_RUN_ID("workflowRunId");

	private final String key;

	SystemVariableKey(String key) {
		this.key = key;
	}

	public static SystemVariableKey get(String key) {
		for (SystemVariableKey value : values()) {
			if (StringUtils.equals(value.getKey(), key)) {
				return value;
			}
		}
		return null;
	}

}