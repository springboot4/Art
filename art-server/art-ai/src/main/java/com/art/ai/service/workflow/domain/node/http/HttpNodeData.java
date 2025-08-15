package com.art.ai.service.workflow.domain.node.http;

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
 * @since 2025/8/10 16:38
 */
@Slf4j
@Data
public class HttpNodeData extends NodeData<HttpNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		log.info("http node process, config: {}, workFLowContext:{}, nodeState:{}", getConfig(), workFlowContext,
				nodeState);
		List<NodeOutputVariable> nodeOutputVariables = List.of(new NodeOutputVariable("h1", "string", "h1"));
		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

}
