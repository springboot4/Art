package com.art.ai.service.workflow;

import cn.hutool.json.JSONUtil;
import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.service.workflow.callback.Callback;
import com.art.ai.service.workflow.callback.CallbackData;
import com.art.ai.service.workflow.callback.CallbackResult;
import com.art.ai.service.workflow.definition.WorkflowsService;
import com.art.ai.service.workflow.dsl.GraphBuilder;
import com.art.ai.service.workflow.dsl.GraphDSL;
import com.art.ai.service.workflow.runtime.WorkflowRuntimeService;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariablePoolManager;
import com.art.json.sdk.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.streaming.StreamingOutput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.art.ai.service.workflow.runtime.WorkFlowStatus.WORKFLOW_PROCESS_STATUS_SUCCESS;

/**
 * @author fxz
 * @since 2025/8/9 19:50
 */
@Slf4j
@RequiredArgsConstructor
public class WorkflowEngine {

	private final WorkflowRuntimeService workflowRuntimeService;

	private final WorkflowsService workflowsService;

	private final List<Callback> callbacks;

	private final Workflow workflow;

	private AiWorkflowRuntimeDTO runtime;

	private AiWorkflowsDTO workflowsDefinition;

	/**
	 * 运行工作流
	 * @param userInputs 用户输入数据
	 * @param conversationId 会话id
	 */
	public void run(Map<String, Object> userInputs, String conversationId) throws GraphStateException {
		// 查询流程定义信息
		this.workflowsDefinition = workflowsService.findById(Long.valueOf(workflow.workflowId()));
		if (Objects.isNull(this.workflowsDefinition)) {
			throw new IllegalArgumentException("工作流定义不存在");
		}

		// 创建工作流运行时
		AiWorkflowRuntimeDTO runtimeCreateDTO = new AiWorkflowRuntimeDTO();
		runtimeCreateDTO.setWorkflowId(workflowsDefinition.getId());
		runtimeCreateDTO.setAppId(workflowsDefinition.getAppId());
		runtimeCreateDTO.setInput(JSONUtil.toJsonStr(userInputs));
		this.runtime = workflowRuntimeService.addAiWorkflowRuntime(runtimeCreateDTO);

		Map<SystemVariableKey, Object> systemVariables = new HashMap<>();
		if (StringUtils.isNoneBlank(conversationId)) {
			systemVariables = Map.of(SystemVariableKey.CONVERSATION_ID, conversationId);
		}
		// 创建变量池
		VariablePool variablePool = VariablePoolManager.createPool(systemVariables,
				JacksonUtil.parseObject(workflowsDefinition.getEnvironmentVariables(), Map.class),
				JacksonUtil.parseObject(workflowsDefinition.getConversationVariables(), Map.class), userInputs);

		// 创建工作流上下文
		WorkFlowContext workFlowContext = new WorkFlowContext();
		workFlowContext.setPool(variablePool);

		AsyncGenerator<NodeOutput<NodeState>> stream = GraphBuilder
			.buildGraph(Objects.requireNonNull(JacksonUtil.parseObject(workflowsDefinition.getGraph(), GraphDSL.class)),
					workFlowContext)
			.compile()
			.stream(userInputs);

		for (NodeOutput<NodeState> out : stream) {
			// todo fxz
			if (out instanceof StreamingOutput<NodeState> streamingOutput) {
				String node = streamingOutput.node();
				String chunk = streamingOutput.chunk();
				log.info("流式输出结果node：{},流式输出内容：{},{}", node, streamingOutput, chunk);
			}
			else {
				String node = out.node();
				Object outputs = out.state().data().get("outputs");
				Object nodeName = out.state().data().get("nodeName");
				CallbackResult callbackResult = CallbackResult.builder()
					.data(CallbackData.builder()
						.outputs(JacksonUtil.toJsonString(outputs))
						.nodeName(String.valueOf(nodeName))
						.nodeId(node)
						.build())
					.build();
				callbacks.forEach(c -> c.execute(callbackResult));
			}
		}

		// 更新运行时变量
		workflowRuntimeService.updateAiWorkflowRuntime(new AiWorkflowRuntimeDTO().setId(this.runtime.getId())
			.setStatus(WORKFLOW_PROCESS_STATUS_SUCCESS)
			.setOutput(JSONUtil.toJsonStr(variablePool)));
	}

	/**
	 * 恢复工作流并继续执行
	 * @param userInput 用户输入数据
	 */
	public void resume(String userInput) {
	}

}
