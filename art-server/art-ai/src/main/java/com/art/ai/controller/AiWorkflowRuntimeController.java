package com.art.ai.controller;

import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowRuntimePageDTO;
import com.art.ai.core.dto.WorkflowRunDTO;
import com.art.ai.service.workflow.WorkflowStarter;
import com.art.ai.service.workflow.runtime.WorkflowRuntimeService;
import com.art.common.security.core.annotation.Ojbk;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bsc.langgraph4j.GraphStateException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @author fxz
 * @date 2025-08-09
 */
@Tag(name = "")
@RestController
@RequestMapping("/ai/workflows/runtime")
@RequiredArgsConstructor
public class AiWorkflowRuntimeController {

	private final WorkflowRuntimeService aiWorkflowRuntimeService;

	private final WorkflowStarter workflowStarter;

	@Ojbk
	@RequestMapping("/ping")
	public Result<String> ping() {
		return Result.success("pong");
	}

	/**
	 * 运行工作流
	 */
	@Ojbk
	@Operation(summary = "运行工作流")
	@PostMapping(value = "/run")
	public SseEmitter run(@RequestBody WorkflowRunDTO workflowRunDTO) throws GraphStateException, InterruptedException {
		return workflowStarter.streaming(workflowRunDTO.getWorkflowId(), workflowRunDTO.getInputs(),
				workflowRunDTO.getConversationId());

		// SseEmitter sseEmitter = new SseEmitter(150000L);
		//
		// ThreadUtil.execAsync(() -> {
		// // 开始处理
		// SSEEmitterHelper.sendStart(sseEmitter);
		//
		// Map<String, Object> map = new HashMap<>();
		// List<NodeOutputVariable> list = List.of(new NodeOutputVariable("name",
		// "string", "开始节点"));
		// map.put("nodeId", "start-node");
		// map.put("nodeName", "开始节点");
		// map.put("outputs", JacksonUtil.toJsonString(list));
		// SSEEmitterHelper.parseAndSendPartialMsg(sseEmitter,
		// JacksonUtil.toJsonString(map));
		//
		// // 流式消息
		// String msg = "我是付绪壮";
		// HashMap<String, Object> tmap = new HashMap<>();
		// for (int i = 0; i < msg.length(); i++) {
		// tmap = new HashMap<>();
		// tmap.put("nodeName", "大模型节点");
		// tmap.put("nodeId", "node_1");
		// tmap.put("chunk", msg.substring(i, i + 1));
		// SSEEmitterHelper.parseAndSendPartialMsg(sseEmitter,
		// JacksonUtil.toJsonString(tmap));
		// try {
		// TimeUnit.SECONDS.sleep(1);
		// } catch (InterruptedException e) {
		// throw new RuntimeException(e);
		// }
		// }
		// tmap.remove("chunk");
		// tmap.put("nodeId", "node_1");
		// tmap.put("status", "completed");
		// SSEEmitterHelper.parseAndSendPartialMsg(sseEmitter,
		// JacksonUtil.toJsonString(tmap));
		//
		// // 纯文本消息
		// map = new HashMap<>();
		// list = List.of(new NodeOutputVariable("name", "string", "最终响应啊"));
		// map.put("nodeId", "node_5");
		// map.put("nodeName", "条件节点");
		// map.put("outputs", JacksonUtil.toJsonString(list));
		// SSEEmitterHelper.parseAndSendPartialMsg(sseEmitter,
		// JacksonUtil.toJsonString(map));
		//
		// map = new HashMap<>();
		// list = List.of(new NodeOutputVariable("age", "string", "18岁"));
		// map.put("nodeId", "node_3");
		// map.put("nodeName", "知识检索节点");
		// map.put("outputs", JacksonUtil.toJsonString(list));
		// SSEEmitterHelper.parseAndSendPartialMsg(sseEmitter,
		// JacksonUtil.toJsonString(map));
		//
		// // 生成结束
		// SSEEmitterHelper.sendComplete(sseEmitter);
		// });
		//
		// return sseEmitter;
	}

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	@PreAuthorize("@ps.hasPermission('system:aiWorkflowRuntime:add')")
	public Result<Boolean> add(@RequestBody AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO) {
		return Result.success(aiWorkflowRuntimeService.addAiWorkflowRuntime(aiWorkflowRuntimeDTO) != null);
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	@PreAuthorize("@ps.hasPermission('system:aiWorkflowRuntime:update')")
	public Result<Boolean> update(@RequestBody AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO) {
		return Result.success(aiWorkflowRuntimeService.updateAiWorkflowRuntime(aiWorkflowRuntimeDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	@PreAuthorize("@ps.hasPermission('system:aiWorkflowRuntime:delete')")
	public Result<Boolean> delete(Long id) {
		return Result.judge(aiWorkflowRuntimeService.deleteAiWorkflowRuntime(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	@PreAuthorize("@ps.hasPermission('system:aiWorkflowRuntime:view')")
	public Result<AiWorkflowRuntimeDTO> findById(Long id) {
		return Result.success(aiWorkflowRuntimeService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	@PreAuthorize("@ps.hasPermission('system:aiWorkflowRuntime:view')")
	public Result<List<AiWorkflowRuntimeDTO>> findAll() {
		return Result.success(aiWorkflowRuntimeService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	@PreAuthorize("@ps.hasPermission('system:aiWorkflowRuntime:view')")
	public Result<PageResult<AiWorkflowRuntimeDTO>> pageAiWorkflowRuntime(
			AiWorkflowRuntimePageDTO aiWorkflowRuntimePageDTO) {
		return Result
			.success(PageResult.success(aiWorkflowRuntimeService.pageAiWorkflowRuntime(aiWorkflowRuntimePageDTO)));
	}

}