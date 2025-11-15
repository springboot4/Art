package com.art.ai.service.agent.runtime.strategy.react;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.core.enums.MessageRoleEnum;
import com.art.ai.service.agent.runtime.AgentPromptRenderer;
import com.art.ai.service.agent.runtime.AgentResponseRoute;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.agent.tool.AgentToolDefinition;
import com.art.ai.service.agent.tool.ToolArgumentDescriptor;
import com.art.core.common.util.CollectionUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REACT模式Prompt构建器
 *
 * @author fxz
 */
@Component
@RequiredArgsConstructor
public class ReactPromptBuilder {

	private final AgentPromptRenderer promptRenderer;

	public List<ChatMessage> buildPrompt(ReactRuntimeState state, List<AgentToolDefinition> toolDefinitions,
			AgentResponseRoute route) {
		List<ChatMessage> messages = new ArrayList<>();

		SystemMessage systemMessage = SystemMessage
			.from(buildSystemPrompt(state.getSpec(), toolDefinitions, route, state));
		messages.add(systemMessage);

		messages.addAll(buildMemoryMessages(state.getMemory()));

		String userContent = buildUserBlock(state);
		messages.add(UserMessage.from(userContent));

		return messages;
	}

	private String buildSystemPrompt(AgentSpec spec, List<AgentToolDefinition> toolDefinitions,
			AgentResponseRoute route, ReactRuntimeState state) {
		List<String> sections = new ArrayList<>();

		if (StringUtils.isNotBlank(spec.getSystemPrompt())) {
			String renderedPrompt = promptRenderer.renderSystemPrompt(spec.getSystemPrompt(), state.getVariablePool(),
					spec);
			sections.add(renderedPrompt.trim());
		}

		sections.add(buildToolCatalog(toolDefinitions));
		sections.add(buildDecisionFormat(route));
		sections.add(buildRules());

		return sections.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining("\n\n"));
	}

	private String buildDecisionFormat(AgentResponseRoute route) {
		if (route == AgentResponseRoute.FUNCTION_CALL) {
			return """
					可用控制函数：
					- agent_final: 返回最终结果
					  参数: { message: 回复内容 }
					""";
		}

		return """
				以JSON格式返回决策：
				{ "kind": "tool_calls", "toolCalls": [{ "name": "...", "args": {...} }] }
				或
				{ "kind": "final", "message": "..." }
				""";
	}

	private String buildRules() {
		return """
				规则：
				1. 需要外部信息时调用工具
				2. 获取工具结果后继续推理
				3. 能够回答用户时返回final
				""";
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

	private String buildUserBlock(ReactRuntimeState state) {
		StringBuilder sb = new StringBuilder();

		sb.append("用户请求：\n").append(state.getUserInput()).append("\n\n");

		if (CollectionUtil.isNotEmpty(state.getSteps())) {
			sb.append("历史执行：\n");
			state.getSteps().forEach(step -> {
				if (CollectionUtil.isNotEmpty(step.getToolCalls())) {
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

		sb.append("请继续执行");

		return sb.toString();
	}

}
