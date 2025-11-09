package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.runtime.dto.AgentControlPayload;
import com.art.ai.service.agent.runtime.dto.AgentFinalFunctionPayload;
import com.art.ai.service.agent.runtime.dto.AgentPlanFunctionPayload;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * 控制函数定义（FunctionCall专用）
 *
 * @author fxz
 */
@Getter
@RequiredArgsConstructor
public enum AgentControlFunction {

	PLAN("agent_plan", buildPlanSpecification(), AgentPlanFunctionPayload.class),

	FINAL("agent_final", buildFinalSpecification(), AgentFinalFunctionPayload.class);

	private final String name;

	private final ToolSpecification specification;

	private final Class<? extends AgentControlPayload> payloadType;

	public static Optional<AgentControlFunction> fromName(String name) {
		if (StringUtils.isBlank(name)) {
			return Optional.empty();
		}

		String normalized = name.toLowerCase(Locale.ROOT);
		return Arrays.stream(values()).filter(item -> item.name.equalsIgnoreCase(normalized)).findFirst();
	}

	public static List<ToolSpecification> specifications() {
		return Arrays.stream(values()).map(AgentControlFunction::getSpecification).toList();
	}

	private static ToolSpecification buildPlanSpecification() {
		JsonObjectSchema stepSchema = JsonObjectSchema.builder()
			.addProperty("step", JsonIntegerSchema.builder().description("步骤序号，从1开始递增").build())
			.addProperty("goal", JsonStringSchema.builder().description("该步骤要达成的目标").build())
			.addProperty("tool", JsonStringSchema.builder().description("可选：建议使用的工具名称").build())
			.required(List.of("step", "goal"))
			.additionalProperties(false)
			.build();

		JsonObjectSchema params = JsonObjectSchema.builder()
			.description("输出详细计划，items 数组描述每一步")
			.addProperty("items", JsonArraySchema.builder().items(stepSchema).description("按顺序排列的计划步骤").build())
			.required(List.of("items"))
			.additionalProperties(false)
			.build();

		return ToolSpecification.builder()
			.name("agent_plan")
			.description("当需要先列出计划时调用，输出 items 数组描述每一步")
			.parameters(params)
			.build();
	}

	private static ToolSpecification buildFinalSpecification() {
		JsonObjectSchema params = JsonObjectSchema.builder()
			.description("输出最终响应，仅包含面向用户的 message")
			.addProperty("message", JsonStringSchema.builder().description("面向用户的文本").build())
			.required(List.of("message"))
			.additionalProperties(false)
			.build();

		return ToolSpecification.builder().name("agent_final").description("当需要给用户最终响应时调用").parameters(params).build();
	}

}
