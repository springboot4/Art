package com.art.ai.service.agent.runtime;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.service.agent.runtime.strategy.AgentStrategy;
import com.art.ai.service.agent.runtime.strategy.AgentStrategyContext;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.agent.spec.AgentSpecJsonHelper;
import com.art.ai.service.agent.tool.AgentTool;
import com.art.ai.service.agent.tool.AgentToolDefinition;
import com.art.ai.service.agent.tool.AgentToolException;
import com.art.ai.service.agent.tool.AgentToolRegistry;
import com.art.ai.service.conversation.variable.ConversationVariableService;
import com.art.ai.service.message.MessageService;
import com.art.ai.service.model.runtime.AiModelRuntimeService;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import com.art.core.common.exception.ArtException;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Agent 执行器 - 策略选择器 负责资源初始化和策略选择，将实际执行逻辑委派给具体策略
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AgentExecutor {

	private final Map<String, AgentStrategy> strategies;

	private final AiModelRuntimeService modelRuntimeService;

	private final MessageService messageService;

	private final AgentToolRegistry toolRegistry;

	private final AgentDecisionParser decisionParser;

	private final ConversationVariableService conversationVariableService;

	private final AgentToolArgumentBinder argumentBinder;

	private final AgentFunctionCallInterpreter functionCallInterpreter;

	/**
	 * 执行Agent
	 * @param agent Agent配置
	 * @param request 运行请求
	 * @param progressCallback 进度回调
	 */
	public AgentRunResult run(AiAgentDTO agent, AgentRunRequest request, AgentProgressCallback progressCallback)
			throws AgentToolException {
		AgentSpec spec = AgentSpecJsonHelper.parse(agent.getSpecJson());
		String runId = StringUtils.isBlank(request.getRunId()) ? UUID.randomUUID().toString() : request.getRunId();

		// 选择策略
		AgentStrategy strategy = selectStrategy(spec);
		log.debug("选择策略: {}", strategy.getStrategyType());

		// 构建执行上下文
		AgentStrategyContext context = buildContext(agent, spec, request, runId, progressCallback);

		// 委派给策略执行
		return strategy.execute(context);
	}

	/**
	 * 根据AgentSpec选择合适的策略
	 */
	private AgentStrategy selectStrategy(AgentSpec spec) {
		String strategyName = spec.getStrategy().getType() == AgentSpec.StrategyType.PLAN_EXECUTE
				? "planExecuteAgentStrategy" : "reactAgentStrategy";

		AgentStrategy strategy = strategies.get(strategyName);
		if (strategy == null) {
			throw new ArtException("未找到策略实现: " + strategyName);
		}
		return strategy;
	}

	/**
	 * 构建策略执行上下文
	 */
	private AgentStrategyContext buildContext(AiAgentDTO agent, AgentSpec spec, AgentRunRequest request, String runId,
			AgentProgressCallback progressCallback) {
		// 加载记忆
		List<AiMessageDTO> memory = loadMemory(spec, request.getConversationId());

		// 加载变量
		Map<String, Object> variables = request.getVariables() == null ? Collections.emptyMap()
				: request.getVariables();
		Map<String, Object> conversationVars = loadConversationVariables(request.getConversationId(), agent.getAppId());

		// 加载工具
		Map<String, AgentTool> enabledTools = resolveEnabledTools(spec.getTools());
		List<AgentToolDefinition> toolDefinitions = enabledTools.values().stream().map(AgentTool::definition).toList();

		// 模型上下文
		AiModelRuntimeContext modelContext = resolveModelContext(spec);
		AgentResponseRoute responseRoute = AgentResponseRoute.select(modelContext.getModel());
		ChatModel chatModel = acquireChatModel(modelContext, spec);

		// 确定策略类型
		boolean isPlanStrategy = spec.getStrategy().getType() == AgentSpec.StrategyType.PLAN_EXECUTE;

		// 构建Function Call工具定义（如果需要）
		List<ToolSpecification> functionToolSpecs = responseRoute == AgentResponseRoute.FUNCTION_CALL
				? buildFunctionCallToolSpecs(toolDefinitions, isPlanStrategy) : Collections.emptyList();

		return AgentStrategyContext.builder()
			.runId(runId)
			.agent(agent)
			.spec(spec)
			.userInput(request.getInput())
			.conversationId(request.getConversationId())
			.memory(memory)
			.variables(variables)
			.conversationVariables(conversationVars)
			.enabledTools(enabledTools)
			.toolDefinitions(toolDefinitions)
			.chatModel(chatModel)
			.responseRoute(responseRoute)
			.functionToolSpecs(functionToolSpecs)
			.decisionParser(decisionParser)
			.argumentBinder(argumentBinder)
			.functionCallInterpreter(functionCallInterpreter)
			.progressCallback(progressCallback)
			.build();
	}

	/**
	 * 解析模型上下文
	 */
	private AiModelRuntimeContext resolveModelContext(AgentSpec spec) {
		return modelRuntimeService.resolveContext(null, Long.valueOf(spec.getModelId()));
	}

	/**
	 * 获取ChatModel实例
	 */
	private ChatModel acquireChatModel(AiModelRuntimeContext context, AgentSpec spec) {
		AiModelInvokeOptions options = AiModelInvokeOptions.builder()
			.temperature(spec.getTemperature() != null ? BigDecimal.valueOf(spec.getTemperature()) : null)
			.maxOutputTokens(spec.getMaxTokens())
			.timeout(Duration.ofSeconds(30))
			.build();
		return modelRuntimeService.acquireChatModel(context, options);
	}

	/**
	 * 解析启用的工具
	 */
	private Map<String, AgentTool> resolveEnabledTools(List<String> toolNames) {
		Map<String, AgentTool> result = new HashMap<>();
		if (toolNames == null || toolNames.isEmpty()) {
			return result;
		}

		for (String name : toolNames) {
			AgentTool tool = toolRegistry.resolve(name);
			if (tool == null) {
				throw new ArtException("未注册的工具: " + name);
			}
			result.put(name, tool);
		}

		return result;
	}

	/**
	 * 加载记忆（历史消息）
	 */
	private List<AiMessageDTO> loadMemory(AgentSpec spec, Long conversationId) {
		AgentSpec.Memory memory = spec.getMemory();
		if (conversationId == null || memory == null || Boolean.FALSE.equals(memory.getEnabled())) {
			return List.of();
		}

		int size = memory.getWindow() != null && memory.getWindow().getSize() != null ? memory.getWindow().getSize()
				: 10;
		return messageService.getRecentMessages(conversationId, size);
	}

	/**
	 * 加载会话变量
	 */
	private Map<String, Object> loadConversationVariables(Long conversationId, Long appId) {
		if (conversationId == null) {
			return Collections.emptyMap();
		}

		return conversationVariableService.initialize(conversationId, appId, Collections.emptyMap()).variables();
	}

	/**
	 * 构建Function Call模式的工具定义
	 */
	private List<ToolSpecification> buildFunctionCallToolSpecs(List<AgentToolDefinition> definitions,
			boolean isPlanStrategy) {
		List<ToolSpecification> specs = new java.util.ArrayList<>();
		if (definitions != null) {
			definitions.forEach(definition -> specs.add(AgentToolSpecificationMapper.toSpecification(definition)));
		}

		// 根据策略类型添加控制函数
		if (isPlanStrategy) {
			// PLAN模式：添加 agent_plan 和 agent_final
			specs.add(AgentControlFunction.PLAN.getSpecification());
			specs.add(AgentControlFunction.FINAL.getSpecification());
		}
		else {
			// REACT模式：仅添加 agent_final
			specs.add(AgentControlFunction.FINAL.getSpecification());
		}

		return specs;
	}

}
