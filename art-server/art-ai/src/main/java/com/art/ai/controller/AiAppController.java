package com.art.ai.controller;

import com.art.ai.core.dto.AiAppDTO;
import com.art.ai.core.dto.AiAppPageDTO;
import com.art.ai.service.app.AiAppService;
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
 * @date 2025-07-25
 */
@Tag(name = "")
@RestController
@RequestMapping("/ai/app")
@RequiredArgsConstructor
public class AiAppController {

	private final AiAppService aiAppService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('ai:aiApp:add')")
	public Result<Boolean> add(@RequestBody AiAppDTO aiAppDTO) {
		return Result.success(aiAppService.addAiApp(aiAppDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('ai:aiApp:update')")
	public Result<Boolean> update(@RequestBody AiAppDTO aiAppDTO) {
		return Result.success(aiAppService.updateAiApp(aiAppDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('ai:aiApp:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiAppService.deleteAiApp(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('ai:aiApp:view')")
	public Result<AiAppDTO> findById(Long id) {
		return Result.success(aiAppService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('ai:aiApp:view')")
	public Result<List<AiAppDTO>> findAll() {
		return Result.success(aiAppService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('ai:aiApp:view')")
	public Result<PageResult<AiAppDTO>> pageAiApp(AiAppPageDTO aiAppPageDTO) {
		return Result.success(PageResult.success(aiAppService.pageAiApp(aiAppPageDTO)));
	}

}