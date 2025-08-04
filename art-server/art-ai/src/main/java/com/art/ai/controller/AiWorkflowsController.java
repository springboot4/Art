package com.art.ai.controller;

import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.core.dto.AiWorkflowsPageDTO;
import com.art.ai.service.AiWorkflowsService;
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
 * @date 2025-07-31
 */
@Tag(name = "")
@RestController
@RequestMapping("/ai/workflows")
@RequiredArgsConstructor
public class AiWorkflowsController {

	private final AiWorkflowsService aiWorkflowsService;

	/**
	 * 根据appId获取最新的工作流信息
	 */
	@Operation(summary = "根据appId获取最新的工作流信息")
	@GetMapping(value = "/findByAppId")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:view')")
	public Result<AiWorkflowsDTO> findByAppId(Long appId) {
		return Result.success(aiWorkflowsService.findByAppId(appId));
	}

	/**
	 * 发布工作流
	 */
	@Operation(summary = "发布工作流")
	@PostMapping(value = "/publish")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:update')")
	public Result<Boolean> publish(@RequestBody AiWorkflowsDTO aiWorkflowsDTO) {
		return Result.success(aiWorkflowsService.publish(aiWorkflowsDTO));
	}

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:add')")
	public Result<Boolean> add(@RequestBody AiWorkflowsDTO aiWorkflowsDTO) {
		return Result.success(aiWorkflowsService.addAiWorkflows(aiWorkflowsDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:update')")
	public Result<Boolean> update(@RequestBody AiWorkflowsDTO aiWorkflowsDTO) {
		return Result.success(aiWorkflowsService.updateAiWorkflows(aiWorkflowsDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiWorkflowsService.deleteAiWorkflows(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:view')")
	public Result<AiWorkflowsDTO> findById(Long id) {
		return Result.success(aiWorkflowsService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:view')")
	public Result<List<AiWorkflowsDTO>> findAll() {
		return Result.success(aiWorkflowsService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('ai:aiWorkflows:view')")
	public Result<PageResult<AiWorkflowsDTO>> pageAiWorkflows(AiWorkflowsPageDTO aiWorkflowsPageDTO) {
		return Result.success(PageResult.success(aiWorkflowsService.pageAiWorkflows(aiWorkflowsPageDTO)));
	}

}