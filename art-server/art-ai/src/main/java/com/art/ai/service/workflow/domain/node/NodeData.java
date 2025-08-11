package com.art.ai.service.workflow.domain.node;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.code.CodeNodeData;
import com.art.ai.service.workflow.domain.node.condition.ConditionNodeData;
import com.art.ai.service.workflow.domain.node.http.HttpNodeData;
import com.art.ai.service.workflow.domain.node.knowledge.KnowledgeNodeData;
import com.art.ai.service.workflow.domain.node.llm.LlmNodeData;
import com.art.ai.service.workflow.domain.node.output.OutputNodeData;
import com.art.ai.service.workflow.domain.node.start.StartNodeData;
import com.art.ai.service.workflow.domain.node.template.TemplateRenderNodeData;
import com.art.ai.service.workflow.domain.node.variable.VariableNodeData;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariableSelector;
import com.art.ai.service.workflow.variable.VariableValue;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 14:48
 */
@Data
@FieldNameConstants
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = NodeData.Fields.nodeType)
@JsonSubTypes({ @JsonSubTypes.Type(value = StartNodeData.class, name = NodeConstants.START_NODE),
		@JsonSubTypes.Type(value = TemplateRenderNodeData.class, name = NodeConstants.TEMPLATE_RENDER_NODE),
		@JsonSubTypes.Type(value = LlmNodeData.class, name = NodeConstants.LLM_NODE),
		@JsonSubTypes.Type(value = CodeNodeData.class, name = NodeConstants.CODE_NODE),
		@JsonSubTypes.Type(value = ConditionNodeData.class, name = NodeConstants.CONDITION_NODE),
		@JsonSubTypes.Type(value = HttpNodeData.class, name = NodeConstants.HTTP_NODE),
		@JsonSubTypes.Type(value = OutputNodeData.class, name = NodeConstants.OUTPUT_NODE),
		@JsonSubTypes.Type(value = KnowledgeNodeData.class, name = NodeConstants.KNOWLEDGE_RETRIEVAL_NODE),
		@JsonSubTypes.Type(value = VariableNodeData.class, name = NodeConstants.VARIABLE_REPLACE_NODE) })
public abstract class NodeData<C extends NodeConfig> {

	private String nodeType;

	protected C config;

	/**
	 * 执行节点逻辑
	 */
	public abstract List<NodeOutputVariable> process(WorkFlowContext workFlowContext, NodeState nodeState);

	// todo fxz 节点内变量定义只有变量名 没有nodeId
	public Map<String, Object> initNodeInputsByReference(VariablePool variablePool, C nodeConfig) {
		List<NodeReferenceParameter> referenceParameters = nodeConfig.getReferenceParameters();
		Map<String, Object> inputs = new HashMap<>();
		referenceParameters.forEach(r -> {
			String parameterName = r.parameterName();
			VariableSelector variableSelector = VariableSelector.of(r.variableType(), r.nodeId(), parameterName);
			VariableValue<?> variableValue = variablePool.get(variableSelector).orElseThrow(RuntimeException::new);
			inputs.put(parameterName, variableValue.getValue());
		});

		return inputs;
	}

}
