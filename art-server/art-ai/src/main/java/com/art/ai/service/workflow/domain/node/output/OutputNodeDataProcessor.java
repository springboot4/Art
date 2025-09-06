package com.art.ai.service.workflow.domain.node.output;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.script.ScriptUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.variable.VariableDataType;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fxz
 * @since 2025/8/10 16:42
 */
@Slf4j
@Data
public class OutputNodeDataProcessor extends NodeDataProcessor<OutputNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		List<NodeOutputVariable> outputVariables = getConfig().getOutputVariables();
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());

		SimpleBindings bindings = new SimpleBindings();
		bindings.putAll(inputs);

		if (CollectionUtil.isEmpty(outputVariables)) {
			log.warn("No output variables defined in OutputNodeConfig");
			return NodeProcessResult.builder().outputVariables(new ArrayList<>()).build();
		}

		List<NodeOutputVariable> variables = outputVariables.stream().map(variable -> {
			String name = variable.getName();
			String format = VariableRenderUtils.render(String.valueOf(variable.getValue()));
			String value = String.valueOf(ScriptUtil.eval(format, bindings));
			return new NodeOutputVariable(name, VariableDataType.STRING, value);
		}).collect(Collectors.toList());
		return NodeProcessResult.builder().outputVariables(variables).build();
	}

}
