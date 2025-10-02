package com.art.ai.controller;

import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.core.dto.document.AiDocumentsPageDTO;
import com.art.ai.service.document.AiDocumentsService;
import com.art.common.security.core.annotation.Ojbk;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @date 2025-09-09
 */
@Tag(name = "")
@RestController
@RequestMapping("/ai/document")
@RequiredArgsConstructor
public class AiDocumentsController {

	private final AiDocumentsService aiDocumentsService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('ai:aiDocuments:add')")
	public Result<Boolean> add(@RequestBody AiDocumentsDTO aiDocumentsDTO) {
		return Result.success(aiDocumentsService.addAiDocuments(aiDocumentsDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('ai:aiDocuments:update')")
	public Result<Boolean> update(@RequestBody AiDocumentsDTO aiDocumentsDTO) {
		return Result.success(aiDocumentsService.updateAiDocuments(aiDocumentsDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('ai:aiDocuments:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiDocumentsService.deleteAiDocuments(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('ai:aiDocuments:view')")
	public Result<AiDocumentsDTO> findById(Long id) {
		return Result.success(aiDocumentsService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('ai:aiDocuments:view')")
	public Result<List<AiDocumentsDTO>> findAll() {
		return Result.success(aiDocumentsService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('ai:aiDocuments:view')")
	public Result<PageResult<AiDocumentsDTO>> pageAiDocuments(AiDocumentsPageDTO aiDocumentsPageDTO) {
		return Result.success(PageResult.success(aiDocumentsService.pageAiDocuments(aiDocumentsPageDTO)));
	}

	/**
	 * 查询文档的图谱信息
	 */
	@Ojbk
	@GetMapping(value = "/graph/info")
	public Result<Map<String, Object>> getDocumentGraphInfo(Long documentId) {
		return Result.success(aiDocumentsService.queryDocumentGraphInfo(documentId));
	}

}