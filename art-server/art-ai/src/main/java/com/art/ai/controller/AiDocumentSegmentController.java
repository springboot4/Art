package com.art.ai.controller;

import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.core.dto.document.AiDocumentSegmentPageDTO;
import com.art.ai.service.dataset.service.AiDocumentSegmentService;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fxz
 * @date 2025-10-02
 */
@Tag(name = "")
@RestController
@RequestMapping("/ai/document/segment")
@RequiredArgsConstructor
public class AiDocumentSegmentController {

	private final AiDocumentSegmentService aiDocumentSegmentService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	public Result<AiDocumentSegmentDTO> add(@RequestBody AiDocumentSegmentDTO aiDocumentSegmentDTO) {
		return Result.success(aiDocumentSegmentService.addAiDocumentSegment(aiDocumentSegmentDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody AiDocumentSegmentDTO aiDocumentSegmentDTO) {
		return Result.success(aiDocumentSegmentService.updateAiDocumentSegment(aiDocumentSegmentDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiDocumentSegmentService.deleteAiDocumentSegment(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<AiDocumentSegmentDTO> findById(Long id) {
		return Result.success(aiDocumentSegmentService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<AiDocumentSegmentDTO>> findAll() {
		return Result.success(aiDocumentSegmentService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<AiDocumentSegmentDTO>> pageAiDocumentSegment(
			AiDocumentSegmentPageDTO aiDocumentSegmentPageDTO) {
		return Result
			.success(PageResult.success(aiDocumentSegmentService.pageAiDocumentSegment(aiDocumentSegmentPageDTO)));
	}

}