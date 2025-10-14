package com.art.ai.service.workflow.domain.node;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.state.AgentState;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/12 13:08
 */
@Builder
@Accessors(chain = true)
@Data
@ToString
public class NodeProcessResult {

	private List<NodeOutputVariable> outputVariables;

	private StreamingChatGenerator<AgentState> streamingOutputs;

	private String next;

}
