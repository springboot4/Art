package com.art.ai.service.agent.spec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

		// 兜底工具列表确保 knowledge_search
		if (Objects.nonNull(spec.getKnowledge()) && !spec.getKnowledge().getDatasetIds().isEmpty()) {
			spec.getTools().add("knowledge_search");
		}
		spec.setTools(spec.getTools().stream().distinct().toList());

		return spec;
	}

}
