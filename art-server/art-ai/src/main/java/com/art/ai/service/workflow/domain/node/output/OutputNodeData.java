package com.art.ai.service.workflow.domain.node.output;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.variable.VariableDataType;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 16:42
 */
@Slf4j
@Data
public class OutputNodeData extends NodeData<OutputNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		OutputNodeConfig nodeConfig = getConfig();
		List<NodeOutputVariable> outputVariables = nodeConfig.getOutputVariables();
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), nodeConfig);
		List<NodeOutputVariable> list = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(outputVariables)) {
			outputVariables.forEach(variable -> {
				String name = variable.getName();
				String value = VariableRenderUtils.format(String.valueOf(variable.getValue()), inputs);
				list.add(new NodeOutputVariable(name, VariableDataType.STRING, value));
			});
		}
		else {
			log.warn("No output variables defined in OutputNodeConfig");
		}

		return NodeProcessResult.builder().outputVariables(list).build();
	}

}
