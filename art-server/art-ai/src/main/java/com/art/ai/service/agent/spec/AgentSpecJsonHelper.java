package com.art.ai.service.agent.spec;

import com.art.ai.service.agent.tool.ToolArgumentBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Agent 规格 JSON 工具
 *
 * @author fxz
 * @since 2025-11-01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AgentSpecJsonHelper {

	private static final ObjectMapper MAPPER = JsonMapper.builder()
		.findAndAddModules()
		.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		.build();

	public static AgentSpec parse(String json) {
		try {
			AgentSpec spec = MAPPER.readValue(json, AgentSpec.class);
			return normalize(spec);
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("解析 Agent 规格失败", ex);
		}
	}

	public static String ensureVersion(String json, String version) {
		AgentSpec spec = parse(json);
		spec.setVersion(version);
		return toJson(spec);
	}

	public static String toJson(AgentSpec spec) {
		try {
			return MAPPER.writeValueAsString(spec);
		}
		catch (JsonProcessingException e) {
			throw new IllegalStateException("序列化 Agent 规格失败", e);
		}
	}

	private static AgentSpec normalize(AgentSpec spec) {
		if (spec == null) {
			throw new IllegalArgumentException("Agent 规格不能为空");
		}
		if (spec.getModelId() == null) {
			throw new IllegalArgumentException("modelId 不能为空");
		}
		if (spec.getTemperature() == null) {
			spec.setTemperature(0.7d);
		}
		if (spec.getStrategy() == null) {
			spec.setStrategy(new AgentSpec.Strategy());
		}
		if (spec.getStrategy().getType() == null) {
			spec.getStrategy().setType(AgentSpec.StrategyType.REACT);
		}
		if (spec.getStrategy().getParams() == null) {
			spec.getStrategy().setParams(Collections.emptyMap());
		}
		if (spec.getStrategy().getDecorators() == null) {
			spec.getStrategy().setDecorators(new ArrayList<>());
		}
		if (spec.getTools() == null) {
			spec.setTools(new ArrayList<>());
		}
		spec.setToolBindings(normalizeBindings(spec.getToolBindings()));
		if (spec.getKnowledge() == null) {
			spec.setKnowledge(new AgentSpec.Knowledge());
		}
		if (spec.getKnowledge().getDatasetIds() == null) {
			spec.getKnowledge().setDatasetIds(new ArrayList<>());
		}
		if (spec.getKnowledge().getRetrievalTypes() == null) {
			spec.getKnowledge().setRetrievalTypes(new ArrayList<>());
		}
		if (spec.getMemory() == null) {
			spec.setMemory(new AgentSpec.Memory());
		}
		if (spec.getMemory().getWindow() == null) {
			spec.getMemory().setWindow(new AgentSpec.Memory.Window());
		}
		if (spec.getMemory().getSummary() == null) {
			spec.getMemory().setSummary(new AgentSpec.Memory.Summary());
		}
		if (spec.getBudgets() == null) {
			spec.setBudgets(new AgentSpec.Budgets());
		}
		if (spec.getBudgets().getMaxSteps() == null || spec.getBudgets().getMaxSteps() <= 0) {
			spec.getBudgets().setMaxSteps(8);
		}
		if (spec.getBudgets().getMaxToolCalls() == null || spec.getBudgets().getMaxToolCalls() <= 0) {
			spec.getBudgets().setMaxToolCalls(8);
		}
		if (spec.getBudgets().getMaxTimeMs() == null || spec.getBudgets().getMaxTimeMs() <= 0) {
			spec.getBudgets().setMaxTimeMs(30000L);
		}
		if (spec.getExtensions() == null) {
			spec.setExtensions(Collections.emptyMap());
		}
		if (spec.getMetadata() == null) {
			spec.setMetadata(Collections.emptyMap());
		}

		// 兜底工具列表确保 knowledge_search
		if (Objects.nonNull(spec.getKnowledge()) && !spec.getKnowledge().getDatasetIds().isEmpty()) {
			spec.getTools().add("knowledge_search");
		}
		spec.setTools(spec.getTools().stream().distinct().toList());

		return spec;
	}

	private static Map<String, List<ToolArgumentBinding>> normalizeBindings(
			Map<String, List<ToolArgumentBinding>> bindings) {
		if (bindings == null || bindings.isEmpty()) {
			return Collections.emptyMap();
		}

		LinkedHashMap<String, List<ToolArgumentBinding>> normalized = new LinkedHashMap<>();
		bindings.forEach((tool, list) -> {
			if (list == null || list.isEmpty()) {
				return;
			}

			normalized.put(tool, List.copyOf(list));
		});
		return Collections.unmodifiableMap(normalized);
	}

}
