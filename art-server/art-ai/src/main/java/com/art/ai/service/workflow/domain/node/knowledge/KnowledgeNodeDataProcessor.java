package com.art.ai.service.workflow.domain.node.knowledge;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 16:50
 */
@Slf4j
@Data
public class KnowledgeNodeDataProcessor extends NodeDataProcessor<KnowledgeNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		log.info("knowledge node process, config: {}, workFLowContext:{}, nodeState:{}", getConfig(), workFlowContext,
				nodeState);
		List<NodeOutputVariable> nodeOutputVariables = List.of(new NodeOutputVariable("k1", "string", "k1"));
		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

}
