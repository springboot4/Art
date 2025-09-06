package com.art.ai.service.workflow.domain.node;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariableSelector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fxz
 * @since 2025/8/24 17:18
 */
public abstract class NodeDataProcessor<C extends NodeConfig> extends NodeData<C> {

	public abstract NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState);

	protected Map<String, Object> initNodeInputsByReference(VariablePool variablePool, C nodeConfig) {
		List<NodeReferenceParameter> referenceParameters = nodeConfig.getReferenceParameters();
		if (referenceParameters == null || referenceParameters.isEmpty()) {
			return Map.of();
		}

		return referenceParameters.stream()
			.collect(Collectors.toMap(NodeReferenceParameter::formatVariableName,
					r -> variablePool.get(VariableSelector.of(r.variableType(), r.nodeId(), r.parameterName()))
						.orElseThrow(() -> new RuntimeException("Variable not found for " + r.parameterName()))
						.getValue()));
	}

}
