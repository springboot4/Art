package com.art.ai.service.workflow.domain.node.template;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 14:58
 */
@Slf4j
@Data
public class TemplateRenderNodeData extends NodeData<TemplateRenderNodeConfig> {

	@Override
	public List<NodeOutputVariable> process(WorkFlowContext workFlowContext, NodeState nodeState) {
		log.info("template render node process, config: {},workFLowContext:{},nodeState:{}", getConfig(),
				workFlowContext, nodeState);
		return List.of(new NodeOutputVariable("t1", "string", "t1"));
	}

}
