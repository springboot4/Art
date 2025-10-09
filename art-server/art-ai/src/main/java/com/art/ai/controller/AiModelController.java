package com.art.ai.controller;

import com.art.ai.core.dto.AiModelDTO;
import com.art.ai.core.dto.AiModelPageDTO;
import com.art.ai.service.model.AiModelService;
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
 * @date 2025-10-09
 */
@Tag(name = "")
@RestController
@RequestMapping("/ai/model")
@RequiredArgsConstructor
public class AiModelController {

	private final AiModelService aiModelService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('system:aiModel:add')")
	public Result<Boolean> add(@RequestBody AiModelDTO aiModelDTO) {
		return Result.success(aiModelService.addAiModel(aiModelDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('system:aiModel:update')")
	public Result<Boolean> update(@RequestBody AiModelDTO aiModelDTO) {
		return Result.success(aiModelService.updateAiModel(aiModelDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('system:aiModel:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiModelService.deleteAiModel(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('system:aiModel:view')")
	public Result<AiModelDTO> findById(Long id) {
		return Result.success(aiModelService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('system:aiModel:view')")
	public Result<List<AiModelDTO>> findAll() {
		return Result.success(aiModelService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('system:aiModel:view')")
	public Result<PageResult<AiModelDTO>> pageAiModel(AiModelPageDTO aiModelPageDTO) {
		return Result.success(PageResult.success(aiModelService.pageAiModel(aiModelPageDTO)));
	}

}