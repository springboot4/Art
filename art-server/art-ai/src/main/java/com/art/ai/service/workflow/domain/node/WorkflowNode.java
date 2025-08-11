package com.art.ai.service.workflow.domain.node;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.variable.VariablePoolManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 14:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowNode<C extends NodeConfig> {

	private String id;

	private String label;

	private NodeData<C> data;

	public List<NodeOutputVariable> run(WorkFlowContext workFlowContext, NodeState nodeState) {
		List<NodeOutputVariable> outputVariables = data.process(workFlowContext, nodeState);
		if (CollectionUtil.isEmpty(outputVariables)) {
			return new ArrayList<>();
		}

		nodeState.setOutputs(outputVariables);

		outputVariables.forEach(variable -> {
			VariablePoolManager.updateNodeOutputVariable(id, variable.getName(), variable.getValue(),
					workFlowContext.getPool());
		});

		return outputVariables;
	}

}
