package com.art.ai.service.workflow.domain.node.llm;

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
 * @since 2025/8/10 15:39
 */
@Slf4j
@Data
public class LlmNodeData extends NodeData<LlmNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		log.info("llm node process, config: {}, workFLowContext:{}, nodeState:{}", getConfig(), workFlowContext,
				nodeState);
		List<NodeOutputVariable> nodeOutputVariables = List
			.of(new NodeOutputVariable("llm_output", "string", "This is a simulated LLM response."));
		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

}
