package com.art.ai.service.agent.runtime.strategy;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.service.agent.runtime.AgentDecisionParser;
import com.art.ai.service.agent.runtime.AgentFunctionCallInterpreter;
import com.art.ai.service.agent.runtime.AgentProgressCallback;
import com.art.ai.service.agent.runtime.AgentResponseRoute;
import com.art.ai.service.agent.runtime.AgentToolArgumentBinder;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.agent.tool.AgentTool;
import com.art.ai.service.agent.tool.AgentToolDefinition;
import com.art.ai.service.workflow.variable.VariablePool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatModel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Agent策略执行上下文 包含执行所需的所有共享资源
 *
 * @author fxz
 */
@Slf4j
@Getter
@Builder
public class AgentStrategyContext {

	/**
	 * 运行ID
	 */
	private final String runId;

	/**
	 * Agent配置
	 */
	private final AiAgentDTO agent;

	/**
	 * Agent规格
	 */
	private final AgentSpec spec;

	/**
	 * 用户输入
	 */
	private final String userInput;

	/**
	 * 会话ID
	 */
	private final Long conversationId;

	/**
	 * 历史消息
	 */
	private final List<AiMessageDTO> memory;

	/**
	 * 统一的变量池 管理所有类型的变量：系统变量、环境变量、会话变量、用户输入变量
	 */
	private final VariablePool variablePool;

	/**
	 * 可用工具映射
	 */
	private final Map<String, AgentTool> enabledTools;

	/**
	 * 工具定义列表
	 */
	private final List<AgentToolDefinition> toolDefinitions;

	/**
	 * 聊天模型
	 */
	private final ChatModel chatModel;

	/**
	 * 响应路由类型
	 */
	private final AgentResponseRoute responseRoute;

	/**
	 * Function Call工具规范
	 */
	private final List<ToolSpecification> functionToolSpecs;

	/**
	 * 决策解析器
	 */
	private final AgentDecisionParser decisionParser;

	/**
	 * 工具参数绑定器
	 */
	private final AgentToolArgumentBinder argumentBinder;

	/**
	 * Function Call解释器
	 */
	private final AgentFunctionCallInterpreter functionCallInterpreter;

	/**
	 * 进度回调（可选）
	 */
	private final AgentProgressCallback progressCallback;

	/**
	 * 安全地通知进度更新
	 * @param type 进度类型
	 * @param data 进度数据
	 */
	public void notifyProgress(String type, Map<String, Object> data) {
		if (progressCallback != null) {
			try {
				progressCallback.onProgress(type, data);
			}
			catch (Exception e) {
				log.warn("进度回调执行异常: type={}, error={}", type, e.getMessage());
			}
		}
	}

}
