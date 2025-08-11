package com.art.ai.service.workflow.domain.node.condition;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 15:40
 */
@Slf4j
@Data
public class ConditionNodeData extends NodeData<ConditionNodeConfig> {

	@Override
	public List<NodeOutputVariable> process(WorkFlowContext workFlowContext, NodeState nodeState) {
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());
		log.info("condition node inputs: {}", inputs);
		log.info("condition node process, config: {}, workFLowContext:{}, nodeState:{}", getConfig(), workFlowContext,
				nodeState);
		return List.of(new NodeOutputVariable("c1", "string", "c1"));
	}

}
