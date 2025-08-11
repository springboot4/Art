package com.art.ai.service.workflow.variable;

import lombok.Getter;

/**
 * @author fxz
 */

@Getter
public enum SystemVariableKey {

	USER_ID("userId"), CONVERSATION_ID("sessionId"), WORKFLOW_ID("workflowId"), WORKFLOW_RUN_ID("workflowRunId");

	private final String key;

	SystemVariableKey(String key) {
		this.key = key;
	}

}