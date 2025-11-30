package com.art.ai.controller;

import com.art.ai.core.convert.KnowledgeRetrievalConvert;
import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.core.dto.dataset.AiDatasetsPageDTO;
import com.art.ai.core.dto.dataset.AiDatasetsUploadDocDTO;
import com.art.ai.core.dto.retrieval.KnowledgeRetrievalDTO;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResponse;
import com.art.ai.service.dataset.rag.retrieval.service.KnowledgeRetrievalService;
import com.art.ai.service.dataset.service.AiDatasetsService;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Tag(name = "数据集管理")
@RestController
@RequestMapping("/ai/dataset")
@RequiredArgsConstructor
public class AiDatasetsController {

	private final AiDatasetsService aiDatasetsService;

	private final KnowledgeRetrievalService knowledgeRetrievalService;

	/**
	 * 知识库召回测试
	 */
	@Operation(summary = "知识库召回测试", description = "支持向量召回、图谱召回和混合召回，包含重排序和融合")
	@PostMapping("/recall/test")
	public Result<RetrievalResponse> testRetrieval(@RequestBody @Validated KnowledgeRetrievalDTO dto) {
		RetrievalRequest request = KnowledgeRetrievalConvert.INSTANCE.toRequest(dto);
		RetrievalResponse response = knowledgeRetrievalService.retrieve(request);
		return Result.success(response);
	}

	/**
	 * 文档存储并索引
	 */
	@Operation(summary = "文档存储并索引")
	@PostMapping("/document")
	public Result<Boolean> document(@RequestBody AiDatasetsUploadDocDTO uploadDoc) {
		return Result.success(aiDatasetsService.uploadDoc(uploadDoc));
	}

	/**
	 * 创建数据集
	 */
	@Operation(summary = "创建数据集")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('ai:aiDatasets:add')")
	public Result<AiDatasetsDTO> add(@RequestBody AiDatasetsDTO aiDatasetsDTO) {
		return Result.success(aiDatasetsService.addAiDatasets(aiDatasetsDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('ai:aiDatasets:update')")
	public Result<Boolean> update(@RequestBody AiDatasetsDTO aiDatasetsDTO) {
		return Result.success(aiDatasetsService.updateAiDatasets(aiDatasetsDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('ai:aiDatasets:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiDatasetsService.deleteAiDatasets(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('ai:aiDatasets:view')")
	public Result<AiDatasetsDTO> findById(Long id) {
		return Result.success(aiDatasetsService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('ai:aiDatasets:view')")
	public Result<List<AiDatasetsDTO>> findAll() {
		return Result.success(aiDatasetsService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('ai:aiDatasets:view')")
	public Result<PageResult<AiDatasetsDTO>> pageAiDatasets(AiDatasetsPageDTO aiDatasetsPageDTO) {
		return Result.success(PageResult.success(aiDatasetsService.pageAiDatasets(aiDatasetsPageDTO)));
	}

}