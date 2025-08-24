package com.art.ai.service.workflow.domain.node;

import com.art.ai.service.workflow.domain.node.code.CodeNodeData;
import com.art.ai.service.workflow.domain.node.condition.ConditionNodeData;
import com.art.ai.service.workflow.domain.node.http.HttpNodeData;
import com.art.ai.service.workflow.domain.node.knowledge.KnowledgeNodeData;
import com.art.ai.service.workflow.domain.node.llm.LlmNodeData;
import com.art.ai.service.workflow.domain.node.output.OutputNodeData;
import com.art.ai.service.workflow.domain.node.start.StartNodeData;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * @author fxz
 * @since 2025/8/10 14:48
 */
@Data
@FieldNameConstants
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = NodeData.Fields.nodeType)
@JsonSubTypes({ @JsonSubTypes.Type(value = StartNodeData.class, name = NodeConstants.START_NODE),
		@JsonSubTypes.Type(value = LlmNodeData.class, name = NodeConstants.LLM_NODE),
		@JsonSubTypes.Type(value = CodeNodeData.class, name = NodeConstants.CODE_NODE),
		@JsonSubTypes.Type(value = ConditionNodeData.class, name = NodeConstants.CONDITION_NODE),
		@JsonSubTypes.Type(value = HttpNodeData.class, name = NodeConstants.HTTP_NODE),
		@JsonSubTypes.Type(value = OutputNodeData.class, name = NodeConstants.OUTPUT_NODE),
		@JsonSubTypes.Type(value = KnowledgeNodeData.class, name = NodeConstants.KNOWLEDGE_RETRIEVAL_NODE) })
public abstract class NodeData<C extends NodeConfig> implements NodeDataProcessor<C> {

	private String nodeType;

	protected C config;

}
