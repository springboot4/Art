package com.art.ai.service.workflow.domain.node.reply;

import cn.hutool.script.ScriptUtil;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.bsc.langgraph4j.state.AgentState;

import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/10/23 13:16
 */
@Slf4j
public class DirectReplyNodeDataProcessor extends NodeDataProcessor<DirectReplyNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		DirectReplyNodeConfig replyNodeConfig = getConfig();
		String replyText = replyNodeConfig.getReplyText();
		Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), replyNodeConfig);

		SimpleBindings bindings = new SimpleBindings();
		bindings.putAll(inputs);

		String format = VariableRenderUtils.render(replyText);
		String value = String.valueOf(ScriptUtil.eval(format, bindings));

		String nodeId = workFlowContext.getCurrentNodeId();
		String nodeLabel = workFlowContext.getCurrentNodeLabel();

		StreamingChatGenerator<AgentState> generator = StreamingChatGenerator.builder()
			.startingNode(nodeId)
			.startingState(nodeState)
			.mapResult(resp -> {
				Map<String, Object> result = new HashMap<>();
				result.put("nodeId", nodeId);
				result.put("nodeName", nodeLabel);
				result.put("status", "completed");
				result.put("outputs", List.of());
				triggerMessageCompletionCallback(workFlowContext, nodeId, nodeLabel, value, resp);
				return result;

			})
			.build();

		generator.handler().onPartialResponse(value);
		generator.handler().onCompleteResponse(ChatResponse.builder().aiMessage(AiMessage.from(value)).build());

		return NodeProcessResult.builder().streamingOutputs(generator).build();
	}

}
