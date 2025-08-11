package com.art.ai.service.workflow;

import cn.hutool.json.JSONUtil;
import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.service.workflow.callback.Callback;
import com.art.ai.service.workflow.definition.WorkflowsService;
import com.art.ai.service.workflow.dsl.GraphBuilder;
import com.art.ai.service.workflow.dsl.GraphDSL;
import com.art.ai.service.workflow.runtime.WorkflowRuntimeService;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariablePoolManager;
import com.art.json.sdk.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.streaming.StreamingOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author fxz
 * @since 2025/8/9 19:50
 */
@Slf4j
@RequiredArgsConstructor
public class WorkflowEngine {

	private final WorkflowRuntimeService workflowRuntimeService;

	private final WorkflowsService workflowsService;

	private final List<Callback> callbacks = new ArrayList<>();

	private final Workflow workflow;

	private AiWorkflowRuntimeDTO runtime;

	private AiWorkflowsDTO workflowsDefinition;

	static String sb = " {\"name\": \"新建AI工作流\", \"edges\": [{\"id\": \"edge_1754929438717\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"start-node\", \"target\": \"node_1\", \"animated\": false, \"sourceHandle\": \"source_handle\", \"targetHandle\": \"target_handle\"}, {\"id\": \"edge_1754929505546\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"node_3\", \"target\": \"node_4\", \"animated\": false, \"sourceHandle\": \"source_handle\", \"targetHandle\": \"target_handle\"}, {\"id\": \"edge_1754929560631\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"node_1\", \"target\": \"node_5\", \"animated\": false, \"sourceHandle\": \"source_handle\", \"targetHandle\": \"target_handle\"}, {\"id\": \"edge_1754929589595\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"node_5\", \"target\": \"node_6\", \"animated\": false, \"sourceHandle\": \"condition1\", \"targetHandle\": \"target_handle\"}, {\"id\": \"edge_1754929643292\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"node_6\", \"target\": \"node_8\", \"animated\": false, \"sourceHandle\": \"source_handle\", \"targetHandle\": \"target_handle\"}, {\"id\": \"edge_1754929667575\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"node_8\", \"target\": \"node_9\", \"animated\": false, \"sourceHandle\": \"source_handle\", \"targetHandle\": \"target_handle\"}, {\"id\": \"edge_1754930097360\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"node_5\", \"target\": \"node_3\", \"animated\": false, \"sourceHandle\": \"else\", \"targetHandle\": \"target_handle\"}, {\"id\": \"edge_1754930103976\", \"type\": \"default\", \"style\": {\"stroke\": \"#666\", \"strokeWidth\": 2}, \"source\": \"node_4\", \"target\": \"node_7\", \"animated\": false, \"sourceHandle\": \"source_handle\", \"targetHandle\": \"target_handle\"}], \"nodes\": [{\"id\": \"start-node\", \"data\": {\"icon\": \"\uD83D\uDE80\", \"color\": \"#52c41a\", \"config\": {\"timeout\": 11, \"retryCount\": 2, \"userInputs\": [{\"name\": \"u1\", \"dataType\": \"string\", \"required\": true, \"description\": \"11\", \"displayName\": \"用户名\"}, {\"name\": \"u2\", \"dataType\": \"number\", \"required\": false, \"description\": \"1\", \"displayName\": \"用户的2\"}], \"referenceParameters\": []}, \"status\": \"idle\", \"nodeType\": \"start\", \"description\": \"工作流的起始点\"}, \"type\": \"customNode\", \"label\": \"开始节点\", \"position\": {\"x\": -79.99999999999997, \"y\": 81.25}}, {\"id\": \"node_1\", \"data\": {\"icon\": \"\uD83E\uDD16\", \"color\": \"#1890ff\", \"config\": {\"model\": \"gpt-3.5-turbo\", \"timeout\": 11, \"maxTokens\": 1024, \"retryCount\": 5, \"temperature\": 0.7, \"userMessage\": \"${u1}\", \"systemPrompt\": \"你是一个有用的AI助手，请根据用户输入提供准确和有帮助的回答。\", \"referenceParameters\": [{\"nodeId\": \"system\", \"variableType\": \"input\", \"parameterName\": \"u2\"}, {\"nodeId\": \"system\", \"variableType\": \"input\", \"parameterName\": \"u1\"}]}, \"status\": \"idle\", \"nodeType\": \"llm\", \"description\": \"GPT/Claude/ChatGLM等AI模型\", \"multiOutput\": false}, \"type\": \"customNode\", \"label\": \"大语言模型\", \"position\": {\"x\": 344.5, \"y\": 43.00000000000006}}, {\"id\": \"node_3\", \"data\": {\"icon\": \"\uD83D\uDCDA\", \"color\": \"#722ed1\", \"config\": {\"topK\": 5, \"query\": \"${baseUrl}${u1}\", \"timeout\": 33, \"threshold\": 0.7, \"retryCount\": 4, \"referenceParameters\": [{\"nodeId\": \"system\", \"variableType\": \"input\", \"parameterName\": \"u1\"}, {\"nodeId\": \"system\", \"variableType\": \"env\", \"parameterName\": \"baseUrl\"}]}, \"status\": \"idle\", \"nodeType\": \"knowledge\", \"description\": \"从向量数据库检索相关知识\", \"multiOutput\": false}, \"type\": \"customNode\", \"label\": \"知识检索\", \"position\": {\"x\": 310.75, \"y\": 309.25}}, {\"id\": \"node_4\", \"data\": {\"icon\": \"\uD83D\uDCBB\", \"color\": \"#13c2c2\", \"config\": {\"code\": \"aaa${documents}\", \"language\": \"python\", \"referenceParameters\": [{\"nodeId\": \"node_3\", \"variableType\": \"output\", \"parameterName\": \"scores\"}, {\"nodeId\": \"node_3\", \"variableType\": \"output\", \"parameterName\": \"documents\"}]}, \"status\": \"idle\", \"nodeType\": \"code\", \"description\": \"执行Python/JavaScript代码\", \"multiOutput\": false}, \"type\": \"customNode\", \"label\": \"代码执行\", \"position\": {\"x\": 664.5, \"y\": 411.74999999999994}}, {\"id\": \"node_5\", \"data\": {\"icon\": \"\uD83D\uDD00\", \"color\": \"#faad14\", \"config\": {\"timeout\": 2, \"conditions\": [{\"id\": \"condition1\", \"label\": \"条件1\", \"expression\": \"${sessionId}==2\", \"description\": \"\"}, {\"id\": \"else\", \"label\": \"其他\", \"expression\": \"else\", \"description\": \"默认分支\"}], \"retryCount\": 3, \"referenceParameters\": [{\"nodeId\": \"system\", \"variableType\": \"sys\", \"parameterName\": \"sessionId\"}]}, \"status\": \"idle\", \"nodeType\": \"condition\", \"description\": \"根据条件分支执行不同逻辑\", \"multiOutput\": true}, \"type\": \"customNode\", \"label\": \"条件判断\", \"position\": {\"x\": 568.25, \"y\": -151.99999999999997}}, {\"id\": \"node_6\", \"data\": {\"icon\": \"\uD83C\uDF10\", \"color\": \"#fa8c16\", \"config\": {\"url\": \"naidu.com\", \"body\": \"{a:\\\"123\\\"}\", \"method\": \"GET\", \"headers\": \"{}\", \"timeout\": 1, \"retryCount\": 2, \"referenceParameters\": []}, \"status\": \"idle\", \"nodeType\": \"http\", \"description\": \"调用外部API接口\", \"multiOutput\": false}, \"type\": \"customNode\", \"label\": \"HTTP请求\", \"position\": {\"x\": 925.75, \"y\": 15.500000000000028}}, {\"id\": \"node_7\", \"data\": {\"icon\": \"\uD83D\uDCDD\", \"color\": \"#eb2f96\", \"config\": {\"timeout\": 2, \"template\": \"根据用户问题: ${question}\\n生成回答: ${output}\", \"retryCount\": 3, \"referenceParameters\": []}, \"status\": \"idle\", \"nodeType\": \"template\", \"description\": \"Jinja2模板渲染文本\", \"multiOutput\": false}, \"type\": \"customNode\", \"label\": \"模板转换\", \"position\": {\"x\": 962, \"y\": 359.25}}, {\"id\": \"node_8\", \"data\": {\"icon\": \"\uD83D\uDD17\", \"color\": \"#722ed1\", \"config\": {\"timeout\": 3, \"variables\": \"{\\\"processed_question\\\": \\\"${question}\\\"}\", \"retryCount\": 4, \"referenceParameters\": []}, \"status\": \"idle\", \"nodeType\": \"variable\", \"description\": \"设置和管理变量\", \"multiOutput\": false}, \"type\": \"customNode\", \"label\": \"变量赋值\", \"position\": {\"x\": 1174.5, \"y\": 75.50000000000003}}, {\"id\": \"node_9\", \"data\": {\"icon\": \"\uD83D\uDCE4\", \"color\": \"#f5222d\", \"config\": {\"outputFormat\": \"text\", \"outputContent\": \"工作流执行完成\", \"referenceParameters\": []}, \"status\": \"idle\", \"nodeType\": \"output\", \"description\": \"工作流的最终输出\", \"multiOutput\": false}, \"type\": \"customNode\", \"label\": \"输出节点\", \"position\": {\"x\": 1308.2499999999998, \"y\": 314.25}}], \"exportTime\": \"2025-08-11T16:38:22.237Z\"}";

	public static void main(String[] args) throws JsonProcessingException, GraphStateException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GraphDSL dsl = objectMapper.readValue(sb, GraphDSL.class);
		log.info(dsl.toString());

		WorkFlowContext workFlowContext = new WorkFlowContext();
		VariablePool variablePool = VariablePoolManager.createPool(
				Map.of(SystemVariableKey.CONVERSATION_ID, "adadasdadadaada"), new HashMap<>(), new HashMap<>(),
				new HashMap<>());
		workFlowContext.setPool(variablePool);
		StateGraph<NodeState> stateGraph = GraphBuilder.buildGraph(dsl, workFlowContext);

		CompiledGraph<NodeState> compile = stateGraph.compile();
		AsyncGenerator<NodeOutput<NodeState>> stream = compile.stream(Map.of("user_name", "fxzzz"));
		for (NodeOutput<NodeState> nodeStateNodeOutput : stream) {
			System.out.println("节点输出结果：" + nodeStateNodeOutput.toString());
			System.out.println("持久化变量" + nodeStateNodeOutput.state());
		}
	}

	/**
	 * 运行工作流
	 * @param userInputs 用户输入数据
	 * @param conversationId 会话id
	 */
	public void run(Map<String, Object> userInputs, String conversationId) throws GraphStateException {
		this.workflowsDefinition = workflowsService.findById(Long.valueOf(workflow.workflowId()));
		if (Objects.isNull(this.workflowsDefinition)) {
			throw new IllegalArgumentException("工作流定义不存在");
		}

		AiWorkflowRuntimeDTO runtime = new AiWorkflowRuntimeDTO();
		runtime.setWorkflowId(workflowsDefinition.getId());
		runtime.setAppId(workflowsDefinition.getAppId());
		runtime.setInput(JSONUtil.toJsonStr(userInputs));
		this.runtime = workflowRuntimeService.addAiWorkflowRuntime(runtime);

		GraphDSL dsl = JacksonUtil.parseObject(workflowsDefinition.getGraph(), GraphDSL.class);

		VariablePool variablePool = VariablePoolManager.createPool(
				Map.of(SystemVariableKey.CONVERSATION_ID, conversationId),
				JacksonUtil.parseObject(workflowsDefinition.getEnvironmentVariables(), Map.class),
				JacksonUtil.parseObject(workflowsDefinition.getConversationVariables(), Map.class), userInputs);

		WorkFlowContext workFlowContext = new WorkFlowContext();
		workFlowContext.setPool(variablePool);
		StateGraph<NodeState> stateGraph = GraphBuilder.buildGraph(Objects.requireNonNull(dsl), workFlowContext);

		CompiledGraph<NodeState> compile = stateGraph.compile();
		AsyncGenerator<NodeOutput<NodeState>> stream = compile.stream(userInputs);
		for (NodeOutput<NodeState> out : stream) {
			// todo fxz
			if (out instanceof StreamingOutput<NodeState> streamingOutput) {
				log.info("流式输出结果：{}", streamingOutput);
			}
			else {
				log.info("节点输出结果：{}", out);
			}
		}

		String poolJson = JSONUtil.toJsonStr(variablePool);
		Long id = runtime.getId();
		workflowRuntimeService.updateAiWorkflowRuntime(new AiWorkflowRuntimeDTO().setId(id).setOutput(poolJson));
	}

	/**
	 * 恢复工作流并继续执行
	 * @param userInput 用户输入数据
	 */
	public void resume(String userInput) {
	}

}
