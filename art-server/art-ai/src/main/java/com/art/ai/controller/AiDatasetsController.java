package com.art.ai.controller;

import com.art.ai.core.dto.AiDatasetsDTO;
import com.art.ai.core.dto.AiDatasetsPageDTO;
import com.art.ai.service.dataset.AiDatasetsService;
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

/**
 * @author fxz
 * @date 2025-09-07
 */
@Tag(name = "")
@RestController
@RequestMapping("/aiDatasets")
@RequiredArgsConstructor
public class AiDatasetsController {

	private final AiDatasetsService aiDatasetsService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('system:aiDatasets:add')")
	public Result<Boolean> add(@RequestBody AiDatasetsDTO aiDatasetsDTO) {
		return Result.success(aiDatasetsService.addAiDatasets(aiDatasetsDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('system:aiDatasets:update')")
	public Result<Boolean> update(@RequestBody AiDatasetsDTO aiDatasetsDTO) {
		return Result.success(aiDatasetsService.updateAiDatasets(aiDatasetsDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('system:aiDatasets:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiDatasetsService.deleteAiDatasets(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('system:aiDatasets:view')")
	public Result<AiDatasetsDTO> findById(Long id) {
		return Result.success(aiDatasetsService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('system:aiDatasets:view')")
	public Result<List<AiDatasetsDTO>> findAll() {
		return Result.success(aiDatasetsService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('system:aiDatasets:view')")
	public Result<PageResult<AiDatasetsDTO>> pageAiDatasets(AiDatasetsPageDTO aiDatasetsPageDTO) {
		return Result.success(PageResult.success(aiDatasetsService.pageAiDatasets(aiDatasetsPageDTO)));
	}

}