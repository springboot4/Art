package com.art.ai.service.workflow.domain.node.condition;

import cn.hutool.script.ScriptUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.variable.VariableDataType;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.script.SimpleBindings;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fxz
 * @since 2025/8/10 15:40
 */
@Slf4j
@Data
public class ConditionNodeDataProcessor extends NodeDataProcessor<ConditionNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());
		try {
			SimpleBindings bindings = new SimpleBindings();
			bindings.putAll(inputs);

			ConditionNodeConfig nodeConfig = getConfig();
			List<ConditionNodeConfig.ConditionConfig> conditions = nodeConfig.getConditions();
			Optional<ConditionNodeConfig.ConditionConfig> configOptional = conditions.stream().filter(condition -> {
				String id = condition.getId();
				if (StringUtils.equals("else", id)) {
					return false;
				}
				String expression = condition.getExpression();
				String render = VariableRenderUtils.render(expression);
				boolean result = (Boolean) ScriptUtil.eval(render, bindings);
				log.info("condition node execute success, condition id: {}, result: {}", id, result);
				return result;
			}).findFirst();

			String id = "else";
			if (configOptional.isPresent()) {
				id = configOptional.get().getId();
			}
			return NodeProcessResult.builder()
				.next(id)
				.outputVariables(List.of(new NodeOutputVariable("分支", VariableDataType.STRING, id)))
				.build();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
