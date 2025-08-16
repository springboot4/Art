package com.art.ai.service.workflow.domain.node.code;

import cn.hutool.script.ScriptUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
import com.art.ai.service.workflow.variable.VariableDataType;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 16:37
 */
@Slf4j
@Data
public class CodeNodeData extends NodeData<CodeNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());
		try {
			SimpleBindings bindings = new SimpleBindings();
			bindings.putAll(inputs);

			String code = VariableRenderUtils.render(getConfig().getCode());
			String result = String.valueOf(ScriptUtil.eval(code, bindings));
			log.info("code node execute success, result: {}", result);

			return NodeProcessResult.builder()
				.outputVariables(List.of(new NodeOutputVariable(WorkflowNode.NodeOutputConstants.OUTPUT,
						VariableDataType.STRING, result)))
				.build();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
