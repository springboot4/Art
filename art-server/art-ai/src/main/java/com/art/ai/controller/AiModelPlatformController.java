package com.art.ai.controller;

import com.art.ai.core.dto.AiModelPlatformDTO;
import com.art.ai.core.dto.AiModelPlatformPageDTO;
import com.art.ai.service.model.AiModelPlatformService;
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
@RequestMapping("/ai/model-platform")
@RequiredArgsConstructor
public class AiModelPlatformController {

	private final AiModelPlatformService aiModelPlatformService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('system:aiModelPlatform:add')")
	public Result<Boolean> add(@RequestBody AiModelPlatformDTO aiModelPlatformDTO) {
		return Result.success(aiModelPlatformService.addAiModelPlatform(aiModelPlatformDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('system:aiModelPlatform:update')")
	public Result<Boolean> update(@RequestBody AiModelPlatformDTO aiModelPlatformDTO) {
		return Result.success(aiModelPlatformService.updateAiModelPlatform(aiModelPlatformDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('system:aiModelPlatform:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiModelPlatformService.deleteAiModelPlatform(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('system:aiModelPlatform:view')")
	public Result<AiModelPlatformDTO> findById(Long id) {
		return Result.success(aiModelPlatformService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('system:aiModelPlatform:view')")
	public Result<List<AiModelPlatformDTO>> findAll() {
		return Result.success(aiModelPlatformService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('system:aiModelPlatform:view')")
	public Result<PageResult<AiModelPlatformDTO>> pageAiModelPlatform(AiModelPlatformPageDTO aiModelPlatformPageDTO) {
		return Result.success(PageResult.success(aiModelPlatformService.pageAiModelPlatform(aiModelPlatformPageDTO)));
	}

}