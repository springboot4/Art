package com.art.ai.service.workflow;

import cn.hutool.json.JSONUtil;
import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.service.conversation.variable.ConversationVariableService;
import com.art.ai.service.conversation.variable.ConversationVariableSnapshot;
import com.art.ai.service.workflow.callback.Callback;
import com.art.ai.service.workflow.callback.CallbackData;
import com.art.ai.service.workflow.callback.CallbackResult;
import com.art.ai.service.workflow.callback.MessageCompletionCallback;
import com.art.ai.service.workflow.definition.WorkflowsService;
import com.art.ai.service.workflow.domain.node.NodeStatus;
import com.art.ai.service.workflow.dsl.GraphBuilder;
import com.art.ai.service.workflow.dsl.GraphDSL;
import com.art.ai.service.workflow.runtime.WorkflowRuntimeService;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariablePoolManager;
import com.art.json.sdk.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.streaming.StreamingOutput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.art.ai.service.workflow.runtime.WorkFlowStatus.WORKFLOW_PROCESS_STATUS_SUCCESS;
import static com.art.core.common.util.CollectionUtil.safeMap;

/**
 * 工作流引擎
 *
 * @author fxz
 * @since 2025/8/9 19:50
 */
@Slf4j
@RequiredArgsConstructor
public class WorkflowEngine {

	private final WorkflowRuntimeService workflowRuntimeService;

	private final WorkflowsService workflowsService;

	private final ConversationVariableService conversationVariableService;

	private final List<Callback> callbacks;

	private final MessageCompletionCallback messageCompletionCallback;

	private final Workflow workflow;

	/**
	 * 运行工作流
	 * @param userInputs 用户输入数据
	 * @param systems 系统变量
	 */
	public void run(Map<String, Object> userInputs, Map<String, Object> systems) throws GraphStateException {
		// 查询流程定义信息
		AiWorkflowsDTO workflowsDefinition = workflowsService.findById(Long.valueOf(workflow.workflowId()));
		if (Objects.isNull(workflowsDefinition)) {
			throw new IllegalArgumentException("工作流定义不存在");
		}

		Map<SystemVariableKey, Object> systemVariables = new HashMap<>();
		for (Map.Entry<String, Object> e : systems.entrySet()) {
			String key = e.getKey();
			Object value = e.getValue();
			SystemVariableKey systemVariableKey = SystemVariableKey.get(key);
			if (Objects.nonNull(systemVariableKey)) {
				systemVariables.put(systemVariableKey, value);
			}
		}

		Map<String, Object> environmentVariables = safeMap(
				JacksonUtil.parseObject(workflowsDefinition.getEnvironmentVariables(), Map.class));
		Map<String, Object> conversationDeclaration = safeMap(
				JacksonUtil.parseObject(workflowsDefinition.getConversationVariables(), Map.class));
		Long conversationId = resolveConversationId(systemVariables);
		ConversationVariableSnapshot snapshot = conversationVariableService.initialize(conversationId,
				workflowsDefinition.getAppId(), conversationDeclaration);
		Map<String, Object> conversationVariables = snapshot.variables();

		// 创建变量池
		VariablePool variablePool = VariablePoolManager.createPool(systemVariables, environmentVariables,
				conversationVariables, userInputs);

		// 创建工作流运行时
		AiWorkflowRuntimeDTO runtimeCreateDTO = new AiWorkflowRuntimeDTO();
		runtimeCreateDTO.setWorkflowId(workflowsDefinition.getId());
		runtimeCreateDTO.setAppId(workflowsDefinition.getAppId());
		runtimeCreateDTO.setInput(JSONUtil.toJsonStr(variablePool));
		AiWorkflowRuntimeDTO runtime = workflowRuntimeService.addAiWorkflowRuntime(runtimeCreateDTO);

		// 创建工作流上下文
		WorkFlowContext workFlowContext = new WorkFlowContext();
		workFlowContext.setPool(variablePool);
		workFlowContext.setRuntime(runtime);
		workFlowContext.setMessageCompletionCallback(messageCompletionCallback);
		workFlowContext.setConversationVariableDeclaration(conversationDeclaration);

		AsyncGenerator<NodeOutput<NodeState>> stream = GraphBuilder
			.buildGraph(Objects.requireNonNull(JacksonUtil.parseObject(workflowsDefinition.getGraph(), GraphDSL.class)),
					workFlowContext)
			.compile()
			.stream(userInputs);
		for (NodeOutput<NodeState> out : stream) {
			if (out instanceof StreamingOutput<NodeState> streamingOutput) {
				String nodeId = streamingOutput.node();
				String chunk = streamingOutput.chunk();
				CallbackResult callbackResult = CallbackResult.builder()
					.data(CallbackData.builder()
						.nodeId(nodeId)
						.chunk(chunk)
						.nodeStatus(NodeStatus.NODE_STATUS_RUNNING)
						.build())
					.build();
				callbacks.forEach(c -> c.execute(callbackResult));
				continue;
			}

			String node = out.node();
			Object outputs = out.state().data().get("outputs");
			Object nodeName = out.state().data().get("nodeName");
			CallbackResult callbackResult = CallbackResult.builder()
				.data(CallbackData.builder()
					.nodeId(node)
					.nodeName(String.valueOf(nodeName))
					.outputs(JacksonUtil.toJsonString(outputs))
					.nodeStatus(NodeStatus.NODE_STATUS_SUCCESS)
					.build())
				.build();
			callbacks.forEach(c -> c.execute(callbackResult));
		}

		if (conversationId != null) {
			try {
				Map<String, Object> snapshotToPersist = conversationVariableService
					.filterByDeclaration(conversationDeclaration, variablePool.snapshotConversationVariables());
				conversationVariableService.persist(conversationId, workflowsDefinition.getAppId(), snapshotToPersist);
			}
			catch (Exception e) {
				log.error("持久化会话变量失败，conversationId={}", conversationId, e);
			}
		}

		// 更新运行时变量
		workflowRuntimeService.updateAiWorkflowRuntime(new AiWorkflowRuntimeDTO().setId(runtime.getId())
			.setStatus(WORKFLOW_PROCESS_STATUS_SUCCESS)
			.setOutput(JSONUtil.toJsonStr(variablePool)));
	}

	private Long resolveConversationId(Map<SystemVariableKey, Object> systemVariables) {
		Object conversationId = systemVariables.get(SystemVariableKey.CONVERSATION_ID);
		if (conversationId == null) {
			return null;
		}
		if (conversationId instanceof Number number) {
			return number.longValue();
		}
		try {
			return Long.valueOf(String.valueOf(conversationId));
		}
		catch (NumberFormatException ex) {
			log.warn("无法解析会话ID: {}", conversationId, ex);
			return null;
		}
	}

}
