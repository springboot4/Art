package com.art.ai.service.workflow.domain.node.output;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 16:42
 */
@Slf4j
@Data
public class OutputNodeData extends NodeData<OutputNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		log.info("output node process, config: {}, workFLowContext:{}, nodeState:{}", getConfig(), workFlowContext,
				nodeState);
		List<NodeOutputVariable> nodeOutputVariables = List.of(new NodeOutputVariable("o1", "string", "o1"));
		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

}
