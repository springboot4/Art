package com.art.ai.controller;

import com.art.ai.core.convert.KnowledgeRetrievalConvert;
import com.art.ai.core.dto.retrieval.KnowledgeRetrievalDTO;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResponse;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.ai.service.dataset.rag.retrieval.service.KnowledgeRetrievalService;
import com.art.common.security.core.annotation.Ojbk;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识库召回测试控制器
 *
 * @author fxz
 * @since 2025/10/05
 */
@Tag(name = "知识库召回测试")
@RestController
@RequestMapping("/ai/knowledge/retrieval")
@RequiredArgsConstructor
public class KnowledgeRetrievalController {

	private final KnowledgeRetrievalService knowledgeRetrievalService;

	/**
	 * 知识库召回测试 - 完整功能
	 */
	@Operation(summary = "知识库召回测试", description = "支持向量召回、图谱召回和混合召回，包含重排序和融合")
	@PostMapping("/test")
	@Ojbk
	// @PreAuthorize("@ps.hasPermission('ai:knowledge:retrieval')")
	public Result<RetrievalResponse> testRetrieval(@RequestBody @Validated KnowledgeRetrievalDTO dto) {
		RetrievalRequest request = KnowledgeRetrievalConvert.toRequest(dto);
		RetrievalResponse response = knowledgeRetrievalService.retrieve(request);
		return Result.success(response);
	}

	/**
	 * 快速向量召回示例
	 */
	@Operation(summary = "快速向量召回", description = "仅使用向量召回，适合快速测试")
	@GetMapping("/quick/vector")
	@PreAuthorize("@ps.hasPermission('ai:knowledge:retrieval')")
	public Result<RetrievalResponse> quickVectorRetrieval(@Parameter(description = "查询文本") @RequestParam String query,
			@Parameter(description = "数据集ID") @RequestParam Long datasetId,
			@Parameter(description = "返回结果数量") @RequestParam(defaultValue = "5") Integer maxResults) {

		// 构建快速向量召回请求
		KnowledgeRetrievalDTO dto = KnowledgeRetrievalDTO.builder()
			.query(query)
			.datasetId(datasetId)
			.retrievalTypes(List.of(RetrievalType.VECTOR))
			.build();

		RetrievalRequest request = KnowledgeRetrievalConvert.toRequest(dto);
		RetrievalResponse response = knowledgeRetrievalService.retrieve(request);
		return Result.success(response);
	}

	/**
	 * 快速图谱召回示例
	 */
	@Operation(summary = "快速图谱召回", description = "仅使用图谱召回，基于实体识别")
	@GetMapping("/quick/graph")
	@PreAuthorize("@ps.hasPermission('ai:knowledge:retrieval')")
	public Result<RetrievalResponse> quickGraphRetrieval(@Parameter(description = "查询文本") @RequestParam String query,
			@Parameter(description = "数据集ID") @RequestParam Long datasetId) {

		// 构建快速图谱召回请求
		KnowledgeRetrievalDTO dto = KnowledgeRetrievalDTO.builder()
			.query(query)
			.datasetId(datasetId)
			.retrievalTypes(List.of(RetrievalType.GRAPH))
			.build();

		RetrievalRequest request = KnowledgeRetrievalConvert.toRequest(dto);
		RetrievalResponse response = knowledgeRetrievalService.retrieve(request);
		return Result.success(response);
	}

	/**
	 * 混合召回示例 - RRF融合策略
	 */
	@Operation(summary = "混合召回示例", description = "向量+图谱混合召回，使用RRF融合策略")
	@PostMapping("/example/hybrid-rrf")
	@PreAuthorize("@ps.hasPermission('ai:knowledge:retrieval')")
	public Result<RetrievalResponse> hybridRrfExample(@RequestBody Map<String, Object> params) {
		String query = (String) params.get("query");
		Long datasetId = Long.valueOf(params.get("datasetId").toString());

		// 构建混合召回请求 - RRF融合
		KnowledgeRetrievalDTO dto = KnowledgeRetrievalDTO.builder()
			.query(query)
			.datasetId(datasetId)
			.retrievalTypes(List.of(RetrievalType.VECTOR, RetrievalType.GRAPH))
			.build();

		RetrievalRequest request = KnowledgeRetrievalConvert.toRequest(dto);
		RetrievalResponse response = knowledgeRetrievalService.retrieve(request);
		return Result.success(response);
	}

	/**
	 * 多样性召回示例 - 避免结果重复
	 */
	@Operation(summary = "多样性召回示例", description = "混合召回并确保结果多样性，避免相似内容重复")
	@PostMapping("/example/diversity")
	@PreAuthorize("@ps.hasPermission('ai:knowledge:retrieval')")
	public Result<RetrievalResponse> diversityExample(@RequestBody Map<String, Object> params) {
		String query = (String) params.get("query");
		Long datasetId = Long.valueOf(params.get("datasetId").toString());
		Integer maxResults = (Integer) params.getOrDefault("maxResults", 8);

		// 构建多样性召回请求
		KnowledgeRetrievalDTO dto = KnowledgeRetrievalDTO.builder()
			.query(query)
			.datasetId(datasetId)
			.retrievalTypes(List.of(RetrievalType.VECTOR, RetrievalType.GRAPH))
			.build();

		RetrievalRequest request = KnowledgeRetrievalConvert.toRequest(dto);

		RetrievalResponse response = knowledgeRetrievalService.retrieve(request);
		return Result.success(response);
	}

	/**
	 * 性能基准测试
	 */
	@Operation(summary = "性能基准测试", description = "测试不同召回策略的性能表现")
	@PostMapping("/benchmark")
	@PreAuthorize("@ps.hasPermission('ai:knowledge:retrieval')")
	public Result<Map<String, Object>> benchmark(@RequestBody Map<String, Object> params) {
		String query = (String) params.get("query");
		Long datasetId = Long.valueOf(params.get("datasetId").toString());

		Map<String, Object> benchmarkResults = new HashMap<>();

		// 测试向量召回
		long startTime = System.currentTimeMillis();
		KnowledgeRetrievalDTO vectorDto = createVectorOnlyDto(query, datasetId);
		RetrievalRequest vectorRequest = KnowledgeRetrievalConvert.toRequest(vectorDto);
		RetrievalResponse vectorResponse = knowledgeRetrievalService.retrieve(vectorRequest);
		long vectorTime = System.currentTimeMillis() - startTime;

		// 测试图谱召回
		startTime = System.currentTimeMillis();
		KnowledgeRetrievalDTO graphDto = createGraphOnlyDto(query, datasetId);
		RetrievalRequest graphRequest = KnowledgeRetrievalConvert.toRequest(graphDto);
		RetrievalResponse graphResponse = knowledgeRetrievalService.retrieve(graphRequest);
		long graphTime = System.currentTimeMillis() - startTime;

		// 测试混合召回
		startTime = System.currentTimeMillis();
		KnowledgeRetrievalDTO hybridDto = createHybridDto(query, datasetId);
		RetrievalRequest hybridRequest = KnowledgeRetrievalConvert.toRequest(hybridDto);
		RetrievalResponse hybridResponse = knowledgeRetrievalService.retrieve(hybridRequest);
		long hybridTime = System.currentTimeMillis() - startTime;

		// 组装基准测试结果
		benchmarkResults.put("vector", Map.of("timeMs", vectorTime, "resultCount", vectorResponse.getTotalCount(),
				"avgScore", calculateAvgScore(vectorResponse)));
		benchmarkResults.put("graph", Map.of("timeMs", graphTime, "resultCount", graphResponse.getTotalCount(),
				"avgScore", calculateAvgScore(graphResponse)));
		benchmarkResults.put("hybrid", Map.of("timeMs", hybridTime, "resultCount", hybridResponse.getTotalCount(),
				"avgScore", calculateAvgScore(hybridResponse)));

		return Result.success(benchmarkResults);
	}

	// 辅助方法
	private KnowledgeRetrievalDTO createVectorOnlyDto(String query, Long datasetId) {
		return KnowledgeRetrievalDTO.builder()
			.query(query)
			.datasetId(datasetId)
			.retrievalTypes(List.of(RetrievalType.VECTOR))
			.build();
	}

	private KnowledgeRetrievalDTO createGraphOnlyDto(String query, Long datasetId) {
		return KnowledgeRetrievalDTO.builder()
			.query(query)
			.datasetId(datasetId)
			.retrievalTypes(List.of(RetrievalType.GRAPH))
			.build();
	}

	private KnowledgeRetrievalDTO createHybridDto(String query, Long datasetId) {
		return KnowledgeRetrievalDTO.builder()
			.query(query)
			.datasetId(datasetId)
			.retrievalTypes(List.of(RetrievalType.VECTOR, RetrievalType.GRAPH))
			.build();
	}

	private double calculateAvgScore(RetrievalResponse response) {
		return response.getResults().stream().mapToDouble(r -> r.getScore().doubleValue()).average().orElse(0.0);
	}

}