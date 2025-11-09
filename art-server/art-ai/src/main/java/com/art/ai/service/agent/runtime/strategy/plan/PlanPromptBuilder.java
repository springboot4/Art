package com.art.ai.service.agent.runtime.strategy.plan;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.service.agent.runtime.AgentPlanItem;
import com.art.ai.service.agent.runtime.AgentResponseRoute;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.agent.tool.AgentToolDefinition;
import com.art.ai.service.agent.tool.ToolArgumentDescriptor;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plan-Execute模式Prompt构建器
 */
@Component
public class PlanPromptBuilder {

	public List<ChatMessage> buildPrompt(PlanRuntimeState state, List<AgentToolDefinition> toolDefinitions,
			AgentResponseRoute route) {
		List<ChatMessage> messages = new ArrayList<>();
		boolean expectPlan = !state.isPlanEstablished();

		messages.add(SystemMessage.from(buildSystemPrompt(state.getSpec(), toolDefinitions, expectPlan, route)));
		messages.addAll(buildMemoryMessages(state.getMemory()));
		messages.add(UserMessage.from(buildUserBlock(state, expectPlan)));

		return messages;
	}

	private String buildSystemPrompt(AgentSpec spec, List<AgentToolDefinition> toolDefinitions, boolean expectPlan,
			AgentResponseRoute route) {
		List<String> sections = new ArrayList<>();

		if (StringUtils.isNotBlank(spec.getSystemPrompt())) {
			sections.add(spec.getSystemPrompt().trim());
		}

		sections.add(buildToolCatalog(toolDefinitions));
		sections.add(buildDecisionFormat(expectPlan, route));
		sections.add(buildRules(expectPlan));

		return sections.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining("\n\n"));
	}

	private String buildDecisionFormat(boolean expectPlan, AgentResponseRoute route) {
		if (route == AgentResponseRoute.FUNCTION_CALL) {
			if (expectPlan) {
				return "可用控制函数：\n- agent_plan: 生成执行计划\n  参数: { items: [{ step: 步骤序号, goal: 目标描述, tool?: 工具名称 }] }";
			}
			return "可用控制函数：\n- agent_final: 返回最终结果\n  参数: { message: 回复内容, requiresUserInput?: 是否需要用户输入 }";
		}

		if (expectPlan) {
			return "以JSON格式返回决策：\n{ \"kind\": \"plan\", \"plan\": [{ \"step\": 1, \"goal\": \"...\", \"tool\": \"...\" }] }";
		}
		return "以JSON格式返回决策：\n{ \"kind\": \"tool_calls\", \"toolCalls\": [{ \"name\": \"...\", \"args\": {...} }] }\n或\n{ \"kind\": \"final\", \"message\": \"...\", \"requiresUserInput\": false }";
	}

	private String buildRules(boolean expectPlan) {
		if (expectPlan) {
			return "规则：\n1. 当前阶段生成计划，不调用工具\n2. 计划应包含明确的步骤和目标\n3. 每个步骤的goal描述该步骤要达成什么";
		}
		return "规则：\n1. 根据计划执行当前步骤\n2. 工具调用失败时可重试或跳过\n3. 需要用户输入时设置requiresUserInput=true";
	}

	private String buildToolCatalog(List<AgentToolDefinition> toolDefinitions) {
		List<String> lines = new ArrayList<>();
		lines.add("可用工具：");

		if (toolDefinitions == null || toolDefinitions.isEmpty()) {
			lines.add("无");
			return String.join("\n", lines);
		}

		toolDefinitions.forEach(def -> lines.addAll(describeTool(def)));
		return String.join("\n", lines);
	}

	private List<String> describeTool(AgentToolDefinition definition) {
		List<String> lines = new ArrayList<>();
		String description = StringUtils.defaultIfBlank(definition.getDescription(), "");
		if (StringUtils.isNotBlank(description)) {
			lines.add("- " + definition.getName() + ": " + description);
		}
		else {
			lines.add("- " + definition.getName());
		}
		if (definition.getArguments() != null && !definition.getArguments().isEmpty()) {
			lines.add("  参数：");
			definition.getArguments().forEach(arg -> lines.add(formatToolArgument(arg)));
		}
		return lines;
	}

	private String formatToolArgument(ToolArgumentDescriptor arg) {
		StringBuilder line = new StringBuilder("    - ");
		line.append(arg.getName()).append(" (").append(StringUtils.defaultString(arg.getType(), "string"));
		line.append(arg.isRequired() ? ", 必填" : ", 可选").append(")");
		if (StringUtils.isNotBlank(arg.getDescription())) {
			line.append(": ").append(arg.getDescription());
		}
		return line.toString();
	}

	private List<ChatMessage> buildMemoryMessages(List<AiMessageDTO> history) {
		List<ChatMessage> messages = new ArrayList<>();
		if (history == null) {
			return messages;
		}
		history.forEach(message -> {
			if (StringUtils.isBlank(message.getContent())) {
				return;
			}
			MessageRoleEnum role = MessageRoleEnum.fromCode(message.getRole());
			switch (role) {
				case USER -> messages.add(UserMessage.from(message.getContent()));
				case ASSISTANT -> messages.add(AiMessage.from(message.getContent()));
				case SYSTEM -> messages.add(SystemMessage.from(message.getContent()));
				default -> {
				}
			}
		});
		return messages;
	}

	private String buildUserBlock(PlanRuntimeState state, boolean expectPlan) {
		StringBuilder sb = new StringBuilder();

		sb.append("用户请求：\n").append(state.getUserInput()).append("\n\n");

		if (state.isPlanEstablished()) {
			sb.append("执行计划：\n");
			sb.append(state.unmodifiablePlan()
				.stream()
				.map(item -> String.format("%d. [%s] %s", item.getStep(), item.getStatus().name(),
						StringUtils.defaultString(item.getGoal())))
				.collect(Collectors.joining("\n")));
			sb.append("\n\n");

			AgentPlanItem activeItem = state.getActivePlanItem();
			if (activeItem != null) {
				sb.append("当前步骤：").append(activeItem.getStep()).append(". ").append(activeItem.getGoal());
				sb.append("\n\n");
			}
		}

		if (!state.getSteps().isEmpty()) {
			sb.append("历史执行：\n");
			state.getSteps().forEach(step -> {
				if (!step.getToolCalls().isEmpty()) {
					step.getToolCalls().forEach(call -> {
						sb.append("工具: ").append(call.getName());
						Object result = step.getObservation().get(call.getName());
						if (result != null) {
							sb.append("\n结果: ").append(result);
						}
						sb.append("\n");
					});
				}
			});
			sb.append("\n");
		}

		sb.append(expectPlan ? "请生成执行计划" : "请继续执行");

		return sb.toString();
	}

}
