package com.art.ai.service.workflow.domain.node;

import cn.hutool.core.collection.CollectionUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.variable.VariablePoolManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author fxz
 * @since 2025/8/10 14:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowNode<C extends NodeConfig> {

	private String id;

	private String label;

	private NodeDataProcessor<C> data;

	public Map<String, Object> run(WorkFlowContext workFlowContext, NodeState nodeState) {
		workFlowContext.setCurrentNodeId(id).setCurrentNodeLabel(label);

		NodeProcessResult process = data.process(workFlowContext, nodeState);
		List<NodeOutputVariable> outputVariables = process.getOutputVariables();

		HashMap<String, Object> res = new HashMap<>();
		res.put("outputs", CollectionUtil.isEmpty(outputVariables) ? new ArrayList<>() : outputVariables);

		if (CollectionUtil.isNotEmpty(outputVariables)) {
			outputVariables.forEach(variable -> {
				VariablePoolManager.updateNodeOutputVariable(id, variable.getName(), variable.getValue(),
						workFlowContext.getPool());
			});
		}

		if (Objects.nonNull(process.getStreamingOutputs())) {
			var generator = process.getStreamingOutputs();
			res.put("_fxz_streaming_messages_", generator);
		}

		if (StringUtils.isNoneBlank(process.getNext())) {
			res.put("next", process.getNext());
		}

		res.put("nodeName", label);
		res.put("nodeId", id);

		return res;
	}

	public interface NodeOutputConstants {

		String OUTPUT = "output";

		String HTTP_RESPONSE_STATUS = "status";

		String HTTP_RESPONSE_BODY = "body";

	}

}
