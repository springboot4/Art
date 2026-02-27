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
import com.art.ai.service.workflow.exception.WorkflowException;
import com.art.ai.service.workflow.exception.WorkflowErrorCode;
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
 * <p>
 * 负责协调工作流的初始化、执行和持久化。采用方法级职责分离，
 * 保持代码简洁的同时确保可读性和可维护性。
 *
 * @author fxz
 * @since 2025/8/9 19:50
 */
@Slf4j
@RequiredArgsConstructor
public class WorkflowEngine {

	// ==================== 常量定义 ====================

	/**
	 * 节点状态数据键 - 输出结果
	 */
	private static final String STATE_KEY_OUTPUTS = "outputs";

	/**
	 * 节点状态数据键 - 节点名称
	 */
	private static final String STATE_KEY_NODE_NAME = "nodeName";

	// ==================== 依赖注入 ====================

	private final WorkflowRuntimeService workflowRuntimeService;

	private final WorkflowsService workflowsService;

	private final ConversationVariableService conversationVariableService;

	private final List<Callback> callbacks;

	private final MessageCompletionCallback messageCompletionCallback;

	private final Workflow workflow;

	// ==================== 公共方法 ====================

	/**
	 * 运行工作流
	 * @param userInputs 用户输入数据
	 * @param systems 系统变量
	 */
	public void run(Map<String, Object> userInputs, Map<String, Object> systems) throws GraphStateException {
		// 1. 初始化工作流上下文
		WorkFlowContext context = initializeWorkflow(userInputs, systems);

		// 2. 执行工作流
		executeWorkflow(context);

		// 3. 持久化结果
		persistWorkflowResults(context);
	}

	// ==================== 初始化阶段 ====================

	/**
	 * 初始化工作流上下文
	 * <p>
	 * 包括：加载工作流定义、解析变量、创建变量池、创建运行时记录
	 */
	private WorkFlowContext initializeWorkflow(Map<String, Object> userInputs, Map<String, Object> systems) {
		// 加载工作流定义
		AiWorkflowsDTO workflowDef = loadWorkflowDefinition();
		Long appId = workflowDef.getAppId();

		// 解析各类变量
		Map<SystemVariableKey, Object> systemVars = parseSystemVariables(systems);
		Map<String, Object> envVars = parseEnvironmentVariables(workflowDef);
		Map<String, Object> conversationDecl = parseConversationDeclaration(workflowDef);
		Long conversationId = resolveConversationId(systemVars);

		// 初始化会话变量
		Map<String, Object> conversationVars = initializeConversationVariables(
				conversationId, appId, conversationDecl);

		// 创建变量池
		VariablePool variablePool = VariablePoolManager.createPool(
				systemVars, envVars, conversationVars, userInputs);

		// 创建运行时记录
		AiWorkflowRuntimeDTO runtime = createRuntimeRecord(workflowDef, variablePool);

		// 构建上下文
		return buildContext(variablePool, runtime, conversationDecl);
	}

	/**
	 * 加载工作流定义
	 */
	private AiWorkflowsDTO loadWorkflowDefinition() {
		AiWorkflowsDTO workflowDef = workflowsService.findById(Long.valueOf(workflow.workflowId()));
		if (Objects.isNull(workflowDef)) {
			throw new WorkflowException(WorkflowErrorCode.WORKFLOW_NOT_FOUND,
					"工作流定义不存在: " + workflow.workflowId());
		}
		return workflowDef;
	}

	/**
	 * 解析系统变量
	 */
	private Map<SystemVariableKey, Object> parseSystemVariables(Map<String, Object> systems) {
		Map<SystemVariableKey, Object> systemVars = new HashMap<>();
		for (Map.Entry<String, Object> e : systems.entrySet()) {
			SystemVariableKey key = SystemVariableKey.get(e.getKey());
			if (Objects.nonNull(key)) {
				systemVars.put(key, e.getValue());
			}
		}
		return systemVars;
	}

	/**
	 * 解析环境变量
	 */
	private Map<String, Object> parseEnvironmentVariables(AiWorkflowsDTO workflowDef) {
		return safeMap(JacksonUtil.parseObject(workflowDef.getEnvironmentVariables(), Map.class));
	}

	/**
	 * 解析会话变量声明
	 */
	private Map<String, Object> parseConversationDeclaration(AiWorkflowsDTO workflowDef) {
		return safeMap(JacksonUtil.parseObject(workflowDef.getConversationVariables(), Map.class));
	}

	/**
	 * 初始化会话变量
	 */
	private Map<String, Object> initializeConversationVariables(
			Long conversationId, Long appId, Map<String, Object> declaration) {
		ConversationVariableSnapshot snapshot = conversationVariableService.initialize(
				conversationId, appId, declaration);
		return snapshot.variables();
	}

	/**
	 * 创建运行时记录
	 */
	private AiWorkflowRuntimeDTO createRuntimeRecord(AiWorkflowsDTO workflowDef, VariablePool variablePool) {
		AiWorkflowRuntimeDTO dto = new AiWorkflowRuntimeDTO();
		dto.setWorkflowId(workflowDef.getId());
		dto.setAppId(workflowDef.getAppId());
		dto.setInput(JSONUtil.toJsonStr(variablePool));
		return workflowRuntimeService.addAiWorkflowRuntime(dto);
	}

	/**
	 * 构建工作流上下文
	 */
	private WorkFlowContext buildContext(VariablePool variablePool, AiWorkflowRuntimeDTO runtime,
			Map<String, Object> conversationDecl) {
		WorkFlowContext context = new WorkFlowContext();
		context.setPool(variablePool);
		context.setRuntime(runtime);
		context.setMessageCompletionCallback(messageCompletionCallback);
		context.setConversationVariableDeclaration(conversationDecl);
		return context;
	}

	// ==================== 执行阶段 ====================

	/**
	 * 执行工作流
	 * <p>
	 * 构建图结构并执行节点，通过回调通知执行进度
	 */
	private void executeWorkflow(WorkFlowContext context) throws GraphStateException {
		AiWorkflowsDTO workflowDef = loadWorkflowDefinition();
		GraphDSL graphDSL = parseGraphDSL(workflowDef);

		AsyncGenerator<NodeOutput<NodeState>> stream = GraphBuilder
				.buildGraph(graphDSL, context)
				.compile()
				.stream(context.getPool().snapshotUserInputVariables());

		processNodeOutputs(stream);
	}

	/**
	 * 解析图 DSL
	 */
	private GraphDSL parseGraphDSL(AiWorkflowsDTO workflowDef) {
		GraphDSL dsl = JacksonUtil.parseObject(workflowDef.getGraph(), GraphDSL.class);
		if (dsl == null) {
			throw new WorkflowException(WorkflowErrorCode.GRAPH_BUILD_FAILED, "图 DSL 解析失败");
		}
		return dsl;
	}

	/**
	 * 处理节点输出流
	 */
	private void processNodeOutputs(AsyncGenerator<NodeOutput<NodeState>> stream) {
		for (NodeOutput<NodeState> out : stream) {
			if (out instanceof StreamingOutput<NodeState> streamingOutput) {
				dispatchStreamingCallback(streamingOutput);
			} else {
				dispatchCompletionCallback(out);
			}
		}
	}

	/**
	 * 分发流式回调
	 */
	private void dispatchStreamingCallback(StreamingOutput<NodeState> streamingOutput) {
		CallbackResult result = CallbackResult.builder()
				.data(CallbackData.builder()
						.nodeId(streamingOutput.node())
						.chunk(streamingOutput.chunk())
						.nodeStatus(NodeStatus.NODE_STATUS_RUNNING)
						.build())
				.build();
		callbacks.forEach(c -> c.execute(result));
	}

	/**
	 * 分发完成回调
	 */
	private void dispatchCompletionCallback(NodeOutput<NodeState> out) {
		Map<String, Object> data = out.state().data();
		CallbackResult result = CallbackResult.builder()
				.data(CallbackData.builder()
						.nodeId(out.node())
						.nodeName(String.valueOf(data.get(STATE_KEY_NODE_NAME)))
						.outputs(JacksonUtil.toJsonString(data.get(STATE_KEY_OUTPUTS)))
						.nodeStatus(NodeStatus.NODE_STATUS_SUCCESS)
						.build())
				.build();
		callbacks.forEach(c -> c.execute(result));
	}

	// ==================== 持久化阶段 ====================

	/**
	 * 持久化工作流结果
	 * <p>
	 * 包括：会话变量持久化、运行时状态更新
	 */
	private void persistWorkflowResults(WorkFlowContext context) {
		Long conversationId = resolveConversationIdFromContext(context);
		persistConversationVariables(context, conversationId);
		updateRuntimeStatus(context);
	}

	/**
	 * 从上下文解析会话 ID
	 */
	private Long resolveConversationIdFromContext(WorkFlowContext context) {
		return resolveConversationId(context.getPool().getSystemVariables());
	}

	/**
	 * 持久化会话变量
	 */
	private void persistConversationVariables(WorkFlowContext context, Long conversationId) {
		if (conversationId == null) {
			return;
		}

		try {
			Map<String, Object> declaration = context.getConversationVariableDeclaration();
			Map<String, Object> snapshot = context.getPool().snapshotConversationVariables();
			Map<String, Object> filtered = conversationVariableService.filterByDeclaration(declaration, snapshot);

			Long appId = context.getRuntime().getAppId();
			conversationVariableService.persist(conversationId, appId, filtered);
		} catch (Exception e) {
			log.error("持久化会话变量失败, conversationId={}", conversationId, e);
			// 不抛出异常，避免影响主流程
		}
	}

	/**
	 * 更新运行时状态
	 */
	private void updateRuntimeStatus(WorkFlowContext context) {
		VariablePool pool = context.getPool();
		AiWorkflowRuntimeDTO runtime = context.getRuntime();

		workflowRuntimeService.updateAiWorkflowRuntime(new AiWorkflowRuntimeDTO()
				.setId(runtime.getId())
				.setStatus(WORKFLOW_PROCESS_STATUS_SUCCESS)
				.setOutput(JSONUtil.toJsonStr(pool)));
	}

	// ==================== 工具方法 ====================

	/**
	 * 解析会话 ID
	 */
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
		} catch (NumberFormatException ex) {
			log.warn("无法解析会话ID: {}", conversationId, ex);
			return null;
		}
	}

}
