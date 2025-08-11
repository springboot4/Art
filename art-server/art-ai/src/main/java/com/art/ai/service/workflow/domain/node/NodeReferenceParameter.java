package com.art.ai.service.workflow.domain.node;

import com.art.ai.service.workflow.variable.VariableType;

/**
 * @author fxz
 * @since 2025/8/11 22:36
 */
public record NodeReferenceParameter(String nodeId, String parameterName, VariableType variableType) {

	public NodeReferenceParameter {
		if (nodeId == null || nodeId.isBlank()) {
			throw new IllegalArgumentException("Node ID cannot be null or blank");
		}
		if (parameterName == null || parameterName.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}
	}
}
