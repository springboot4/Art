package com.art.ai.service.agent.tool.impl;

import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.agent.tool.AgentTool;
import com.art.ai.service.agent.tool.AgentToolDefinition;
import com.art.ai.service.agent.tool.AgentToolException;
import com.art.ai.service.agent.tool.AgentToolRequest;
import com.art.ai.service.agent.tool.AgentToolResult;
import com.art.ai.service.agent.tool.ToolArgumentDescriptor;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResponse;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.ai.service.dataset.rag.retrieval.service.KnowledgeRetrievalService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * knowledge_search 工具
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KnowledgeSearchTool implements AgentTool {

	public static final String NAME = "knowledge_search";

	private final KnowledgeRetrievalService retrievalService;

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public AgentToolDefinition definition() {
		return AgentToolDefinition.builder()
			.name(NAME)
			.description("按需访问企业知识库，返回高相关片段")
			.timeout(Duration.ofSeconds(15))
			.arguments(List.of(
					ToolArgumentDescriptor.builder()
						.name("query")
						.type("string")
						.required(true)
						.description("检索语句，需准确表达需求")
						.build(),
					ToolArgumentDescriptor.builder()
						.name("datasetIds")
						.type("array<long>")
						.required(false)
						.description("可选：覆盖默认数据集")
						.build(),
					ToolArgumentDescriptor.builder()
						.name("retrievalTypes")
						.type("array<string>")
						.required(false)
						.description("可选：VECTOR/QA/HYBRID 等检索模式")
						.build(),
					ToolArgumentDescriptor.builder()
						.name("limit")
						.type("integer")
						.required(false)
						.description("可选：返回片段上限")
						.build()))
			.build();
	}

	@Override
	public AgentToolResult invoke(AgentToolRequest request) throws AgentToolException {
		long start = System.nanoTime();
		JsonNode args = request.getArguments();
		String query = args != null && args.hasNonNull("query") ? args.get("query").asText() : null;
		if (StringUtils.isBlank(query)) {
			throw new AgentToolException("knowledge_search 需要 query 参数");
		}

		AgentSpec spec = request.getAgentSpec();
		List<Long> datasetIds = resolveDatasetIds(spec, args);
		if (datasetIds.isEmpty()) {
			throw new AgentToolException("knowledge_search 未配置可用的数据集");
		}

		List<RetrievalType> retrievalTypes = resolveRetrievalTypes(spec, args);
		if (retrievalTypes.isEmpty()) {
			retrievalTypes = List.of(RetrievalType.VECTOR);
		}

		try {
			RetrievalRequest retrievalRequest = RetrievalRequest.builder()
				.query(query)
				.datasetIds(datasetIds)
				.retrievalTypes(retrievalTypes)
				.build();

			RetrievalResponse response = retrievalService.retrieve(retrievalRequest);
			List<Map<String, Object>> passages = toPassages(response.getResults());

			Map<String, Object> data = new HashMap<>();
			data.put("query", response.getQuery());
			data.put("passages", passages);
			data.put("total", response.getTotalCount());

			long elapsed = (System.nanoTime() - start) / 1_000_000;
			return AgentToolResult.builder().ok(true).data(data).elapsedMs(elapsed).build();
		}
		catch (Exception ex) {
			throw new AgentToolException("知识检索失败: " + ex.getMessage(), ex);
		}
	}

	private List<Long> resolveDatasetIds(AgentSpec spec, JsonNode args) {
		List<Long> datasetIds = new ArrayList<>();
		if (args != null && args.has("datasetIds") && args.get("datasetIds").isArray()) {
			ArrayNode array = (ArrayNode) args.get("datasetIds");
			array.forEach(node -> datasetIds.add(node.asLong()));
		}
		if (datasetIds.isEmpty() && spec.getKnowledge() != null) {
			datasetIds.addAll(spec.getKnowledge().getDatasetIds().stream().map(Long::parseLong).toList());
		}
		return datasetIds;
	}

	private List<RetrievalType> resolveRetrievalTypes(AgentSpec spec, JsonNode args) {
		List<String> rawTypes = new ArrayList<>();
		if (args != null && args.has("retrievalTypes")) {
			args.get("retrievalTypes").forEach(node -> rawTypes.add(node.asText()));
		}
		if (rawTypes.isEmpty() && spec.getKnowledge() != null) {
			rawTypes.addAll(spec.getKnowledge().getRetrievalTypes());
		}
		return rawTypes.stream()
			.map(type -> RetrievalType.fromValue(type.toLowerCase(Locale.ROOT)))
			.collect(Collectors.toList());
	}

	private List<Map<String, Object>> toPassages(List<RetrievalResult> results) {
		List<Map<String, Object>> passages = new ArrayList<>();
		if (results == null) {
			return passages;
		}
		for (RetrievalResult result : results) {
			Map<String, Object> passage = new HashMap<>();
			passage.put("id", result.getSegmentId() != null ? result.getSegmentId() : result.getDocumentId());
			passage.put("text", result.getContent());
			passage.put("score", result.getScore());
			passage.put("metadata", result.getMetadata());
			passages.add(passage);
		}
		return passages;
	}

}
