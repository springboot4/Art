package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.workflow.domain.node.NodeInputVariable;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import com.art.ai.service.workflow.variable.VariableSelector;
import com.art.ai.service.workflow.variable.VariableType;
import com.art.ai.service.workflow.variable.VariableValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Agent Prompt 渲染器 负责将变量注入到 Prompt 中 复用工作流的 VariableRenderUtils 机制
 *
 * @author fxz
 * @since 2025-11-15
 */
@Slf4j
@Component
public class AgentPromptRenderer {

	/**
	 * 渲染 System Prompt 将变量占位符替换为实际值 格式: ${input.input.age} -> 30
	 * @param systemPrompt 系统提示词模板
	 * @param variablePool 变量池
	 * @param spec Agent 规格（用于获取变量定义）
	 * @return 渲染后的提示词
	 */
	public String renderSystemPrompt(String systemPrompt, VariablePool variablePool, AgentSpec spec) {
		if (StringUtils.isBlank(systemPrompt)) {
			log.debug("系统提示词为空，跳过渲染");
			return "";
		}

		if (variablePool == null) {
			log.warn("变量池为空，直接返回原始提示词");
			return systemPrompt;
		}

		if (spec == null) {
			log.warn("AgentSpec 为空，直接返回原始提示词");
			return systemPrompt;
		}

		try {
			// 构建渲染上下文
			Map<String, Object> renderContext = buildRenderContext(variablePool, spec);

			// 使用 VariableRenderUtils.format 渲染
			String rendered = VariableRenderUtils.format(systemPrompt, renderContext);

			log.debug("系统提示词渲染完成，变量数: {}", renderContext.size());
			return rendered;
		}
		catch (Exception e) {
			log.error("系统提示词渲染失败: {}", e.getMessage(), e);
			return systemPrompt;
		}
	}

	/**
	 * 构建渲染上下文 从 VariablePool 提取变量，转换为扁平 Map 供模板引擎使用 本次只处理用户输入变量，预留其他变量类型扩展点
	 * @param variablePool 变量池
	 * @param spec Agent 规格
	 * @return 渲染上下文
	 */
	private Map<String, Object> buildRenderContext(VariablePool variablePool, AgentSpec spec) {
		Map<String, Object> context = new HashMap<>();

		if (spec.getUserInputs() != null && !spec.getUserInputs().isEmpty()) {
			extractUserInputVariables(variablePool, spec, context);
		}

		return context;
	}

	/**
	 * 提取用户输入变量 格式: input.input.xxx -> input_input_xxx
	 * @param variablePool 变量池
	 * @param spec Agent 规格
	 * @param context 渲染上下文
	 */
	private void extractUserInputVariables(VariablePool variablePool, AgentSpec spec, Map<String, Object> context) {
		for (NodeInputVariable userInput : spec.getUserInputs()) {
			try {
				// 构建变量选择器
				VariableSelector selector = VariableSelector.of(VariableType.USER_INPUT,
						VariableType.USER_INPUT.getType(), userInput.name());

				// 从变量池获取值
				Object value = variablePool.get(selector).map(VariableValue::getValue).orElse(null);

				// 格式化为 input_input_xxx (对齐 VariableRenderUtils 的命名转换规则)
				String key = formatUserInputVariableName(userInput.name());
				context.put(key, value);

				log.trace("提取用户输入变量: {} = {}", key, value);
			}
			catch (Exception e) {
				log.warn("提取用户输入变量失败: {}, 错误: {}", userInput.name(), e.getMessage());
			}
		}
	}

	/**
	 * 格式化用户输入变量名 对齐工作流的变量命名规则
	 * @param variableName 变量名
	 * @return 格式化后的变量名
	 */
	private String formatUserInputVariableName(String variableName) {
		// 对应 Prompt 中的 ${input.input.variableName}
		return String.format("input_input_%s", variableName);
	}

}
