package com.art.ai.controller;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.AiAgentPageDTO;
import com.art.ai.core.dto.conversation.AgentRunDTO;
import com.art.ai.service.agent.AgentApplicationService;
import com.art.ai.service.agent.AgentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * Agent 管理接口
 *
 * @author fxz
 * @since 2025-11-01
 */
@Tag(name = "Agent管理")
@RestController
@RequestMapping("/ai/agent")
@RequiredArgsConstructor
public class AiAgentController {

	private final AgentService agentService;

	private final AgentApplicationService agentApplicationService;

	@Operation(summary = "保存草稿")
	@PostMapping("/draft")
	@PreAuthorize("@ps.hasPermission('ai:agent:add')")
	public Result<Boolean> saveDraft(@RequestBody AiAgentDTO dto) {
		return Result.success(agentService.saveDraft(dto));
	}

	@Operation(summary = "发布Agent")
	@PostMapping("/publish")
	@PreAuthorize("@ps.hasPermission('ai:agent:update')")
	public Result<Boolean> publish(@RequestBody AiAgentDTO dto) {
		return Result.success(agentService.publish(dto));
	}

	@Operation(summary = "新增Agent")
	@PostMapping("/add")
	@PreAuthorize("@ps.hasPermission('ai:agent:add')")
	public Result<Boolean> add(@RequestBody AiAgentDTO dto) {
		return Result.success(agentService.add(dto));
	}

	@Operation(summary = "更新Agent")
	@PostMapping("/update")
	@PreAuthorize("@ps.hasPermission('ai:agent:update')")
	public Result<Boolean> update(@RequestBody AiAgentDTO dto) {
		return Result.success(agentService.update(dto));
	}

	@Operation(summary = "删除Agent")
	@DeleteMapping("/delete")
	@PreAuthorize("@ps.hasPermission('ai:agent:delete')")
	public Result<Boolean> delete(@RequestParam Long id) {
		return Result.success(agentService.delete(id));
	}

	@Operation(summary = "根据ID查询Agent")
	@GetMapping("/findById")
	@PreAuthorize("@ps.hasPermission('ai:agent:view')")
	public Result<AiAgentDTO> findById(@RequestParam Long id) {
		return Result.success(agentService.findById(id));
	}

	@Operation(summary = "根据App查询已发布Agent")
	@GetMapping("/published")
	@PreAuthorize("@ps.hasPermission('ai:agent:view')")
	public Result<AiAgentDTO> findPublished(@RequestParam Long appId) {
		return Result.success(agentService.findPublishedByAppId(appId));
	}

	@Operation(summary = "根据App查询草稿Agent")
	@GetMapping("/draft")
	@PreAuthorize("@ps.hasPermission('ai:agent:view')")
	public Result<AiAgentDTO> findDraft(@RequestParam Long appId) {
		return Result.success(agentService.findDraftByAppId(appId));
	}

	@Operation(summary = "根据appid查询最新的Agent")
	@GetMapping("/last")
	@PreAuthorize("@ps.hasPermission('ai:agent:view')")
	public Result<AiAgentDTO> findLastAgent(@RequestParam Long appId) {
		return Result.success(agentService.findLastAgent(appId));
	}

	@Operation(summary = "列出应用下所有Agent")
	@GetMapping("/list")
	@PreAuthorize("@ps.hasPermission('ai:agent:view')")
	public Result<List<AiAgentDTO>> list(@RequestParam Long appId) {
		return Result.success(agentService.listByAppId(appId));
	}

	@Operation(summary = "分页查询Agent")
	@GetMapping("/page")
	@PreAuthorize("@ps.hasPermission('ai:agent:view')")
	public Result<PageResult<AiAgentDTO>> page(AiAgentPageDTO pageDTO) {
		return Result.success(PageResult.success(agentService.page(pageDTO)));
	}

	@Ojbk
	@Operation(summary = "运行Agent")
	@PostMapping("/run")
	public SseEmitter run(@RequestBody AgentRunDTO dto) {
		return agentApplicationService.run(dto);
	}

}
