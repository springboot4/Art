package com.art.ai.service.workflow.domain.node.variable;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 16:52
 */
@Slf4j
@Data
public class VariableNodeData extends NodeData<VariableNodeConfig> {

	private Object variables;

	@Override
	public List<NodeOutputVariable> process(WorkFlowContext workFlowContext, NodeState nodeState) {
		log.info("variable node process, variables: {},workFLowContext:{},nodeState:{}", variables, workFlowContext,
				nodeState);
		return List.of(new NodeOutputVariable("k1", "string", "v1"), new NodeOutputVariable("k2", "string", "v2"));
	}

}
