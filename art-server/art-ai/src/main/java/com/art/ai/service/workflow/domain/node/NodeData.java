package com.art.ai.service.workflow.domain.node;

import com.art.ai.service.workflow.domain.node.code.CodeNodeDataProcessor;
import com.art.ai.service.workflow.domain.node.condition.ConditionNodeDataProcessor;
import com.art.ai.service.workflow.domain.node.http.HttpNodeDataProcessor;
import com.art.ai.service.workflow.domain.node.knowledge.KnowledgeNodeDataProcessor;
import com.art.ai.service.workflow.domain.node.llm.LlmAnswerNodeDataProcessor;
import com.art.ai.service.workflow.domain.node.llm.LlmNodeDataProcessor;
import com.art.ai.service.workflow.domain.node.output.OutputNodeDataProcessor;
import com.art.ai.service.workflow.domain.node.start.StartNodeDataProcessor;
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
@JsonSubTypes({ @JsonSubTypes.Type(value = StartNodeDataProcessor.class, name = NodeConstants.START_NODE),
		@JsonSubTypes.Type(value = LlmNodeDataProcessor.class, name = NodeConstants.LLM_NODE),
		@JsonSubTypes.Type(value = LlmAnswerNodeDataProcessor.class, name = NodeConstants.LLM_ANSWER_NODE),
		@JsonSubTypes.Type(value = CodeNodeDataProcessor.class, name = NodeConstants.CODE_NODE),
		@JsonSubTypes.Type(value = ConditionNodeDataProcessor.class, name = NodeConstants.CONDITION_NODE),
		@JsonSubTypes.Type(value = HttpNodeDataProcessor.class, name = NodeConstants.HTTP_NODE),
		@JsonSubTypes.Type(value = OutputNodeDataProcessor.class, name = NodeConstants.OUTPUT_NODE),
		@JsonSubTypes.Type(value = KnowledgeNodeDataProcessor.class, name = NodeConstants.KNOWLEDGE_RETRIEVAL_NODE) })
public abstract class NodeData<C extends NodeConfig> {

	private String nodeType;

	protected C config;

}
