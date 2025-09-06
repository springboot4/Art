package com.art.ai.service.workflow.domain.node.start;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 14:54
 */
@Slf4j
@Data
public class StartNodeDataProcessor extends NodeDataProcessor<StartNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		return NodeProcessResult.builder().outputVariables(List.of()).build();
	}

}
