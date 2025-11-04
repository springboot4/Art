package com.art.ai.service.agent.runtime;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.core.enums.MessageRoleEnum;
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
 * Agent Prompt 构建器
 *
 * @author fxz
 * @since 2025-11-01
 */
@Component
public class AgentPromptBuilder {

	private static final String ROLE_DIRECTIVE = "你是企业级智能助手。所有外部事实必须通过允许的工具获取，禁止臆测。";

	private static final String JSON_FORMAT_BLOCK = String.join("\n", "始终返回严格的 JSON，结构如下：", "{",
			"  \"kind\": \"tool_calls|final|plan|ask_user|halt\",", "  \"thought\": \"...\",",
			"  \"tool_calls\": [{\"name\": \"\", \"args\": {}}],", "  \"message\": \"最终回复或给用户的说明\",",
			"  \"citations\": [\"引用标识\"],", "  \"plan\": [{\"step\":1,\"goal\":\"\",\"tool\":\"可选\"}]", "}");

	private static final String BASE_BEHAVIOR_RULES = String.join("\n",
			"当需要调用工具时设置 kind=\"tool_calls\"，可一次调用多个工具；当可以直接回答或告知缺口时使用 kind=\"final\"；若需先列出计划则输出 kind=\"plan\" 并提供 plan 数组。",
			"所有 tool_calls 中的 args 必须是有效 JSON，且只包含必要字段。", "如果缺少必需信息但可以由用户提供，请使用 kind=\"ask_user\" 并在 message 中说明需求。",
			"如达到预算或需要人工处理，使用 kind=\"halt\" 并在 message 中给出缺口说明。");

	public List<ChatMessage> buildPrompt(AgentRuntimeState state, List<AgentToolDefinition> toolDefinitions,
			boolean expectPlan) {
		List<ChatMessage> messages = new ArrayList<>();

		SystemMessage systemMessage = SystemMessage
			.from(buildSystemPrompt(state.getSpec(), toolDefinitions, expectPlan));
		messages.add(systemMessage);

		messages.addAll(buildMemoryMessages(state.getMemory()));

		String userContent = buildUserBlock(state, expectPlan);
		messages.add(UserMessage.from(userContent));

		return messages;
	}

	private String buildSystemPrompt(AgentSpec spec, List<AgentToolDefinition> toolDefinitions, boolean expectPlan) {
		List<String> sections = new ArrayList<>();
		if (StringUtils.isNotBlank(spec.getSystemPrompt())) {
			sections.add(spec.getSystemPrompt().trim());
		}

		sections.add(ROLE_DIRECTIVE);

		sections.add(buildJsonGuidance(expectPlan));

		sections.add(buildToolCatalog(toolDefinitions));

		return sections.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining("\n\n"));
	}

	private String buildJsonGuidance(boolean expectPlan) {
		List<String> parts = new ArrayList<>();
		parts.add(JSON_FORMAT_BLOCK);

		parts.add(BASE_BEHAVIOR_RULES);

		if (expectPlan) {
			parts.add("当前必须先输出 kind=\"plan\" 的计划，随后等待系统提示继续执行。");
		}

		return String.join("\n\n", parts);
	}

	private String buildToolCatalog(List<AgentToolDefinition> toolDefinitions) {
		List<String> lines = new ArrayList<>();
		lines.add("可用工具列表：");
		if (toolDefinitions == null || toolDefinitions.isEmpty()) {
			lines.add("- 无工具可用，如需外部信息请说明缺口。");
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
			lines.add("  模型填写：");
			definition.getArguments().forEach(arg -> lines.add(formatToolArgument(arg)));
		}
		return lines;
	}

	private String formatToolArgument(ToolArgumentDescriptor arg) {
		StringBuilder line = new StringBuilder("    - ");
		line.append(arg.getName()).append(" (").append(StringUtils.defaultString(arg.getType(), ""));
		line.append(arg.isRequired() ? ", 必填" : ", 可选").append(")");
		if (StringUtils.isNotBlank(arg.getDescription())) {
			line.append(" - ").append(arg.getDescription());
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

	private String buildUserBlock(AgentRuntimeState state, boolean expectPlan) {
		StringBuilder sb = new StringBuilder();
		sb.append("用户当前请求：\n").append(state.getUserInput()).append("\n\n");
		if (state.isPlanEstablished()) {
			sb.append("已确认的计划：\n");
			sb.append(state.unmodifiablePlan()
				.stream()
				.map(item -> item.getStep() + ". " + StringUtils.defaultString(item.getGoal()))
				.collect(Collectors.joining("\n")));
			sb.append("\n\n");
		}

		if (!state.getSteps().isEmpty()) {
			sb.append("历史步骤：\n");
			state.getSteps().forEach(step -> {
				sb.append("步骤 ").append(step.getIndex()).append(" (kind=").append(step.getDecisionKind()).append(")\n");
				sb.append("Thought: ").append(StringUtils.defaultString(step.getThought(), "无")).append("\n");
				if (!step.getToolCalls().isEmpty()) {
					step.getToolCalls()
						.forEach(call -> sb.append("Action: ")
							.append(call.getName())
							.append(" -> ")
							.append(call.getArguments())
							.append("\n"));
				}
				if (!step.getObservation().isEmpty()) {
					sb.append("Observation: ").append(step.getObservation()).append("\n");
				}
				sb.append("---\n");
			});
			sb.append("\n");
		}

		sb.append("请根据以上信息给出下一步 JSON 决策。");
		if (expectPlan) {
			sb.append("当前阶段仅生成 plan 数组，暂不调用工具。\n");
		}

		return sb.toString();
	}

}
