package com.art.ai.controller;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.core.dto.conversation.MessageQueryDTO;
import com.art.ai.core.dto.conversation.SendMsgDTO;
import com.art.ai.service.message.MessageService;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * AI消息管理Controller
 *
 * @author fxz
 * @date 2025-10-18
 */
@Tag(name = "AI消息管理")
@RestController
@RequestMapping("/ai/message")
@RequiredArgsConstructor
public class AiMessageController {

	private final MessageService messageService;

	/**
	 * 发送消息
	 */
	@Operation(summary = "发送消息")
	@PostMapping("/send")
	public SseEmitter sendMessages(@RequestBody SendMsgDTO sendMsgDTO) {
		return messageService.sendMessages(sendMsgDTO);
	}

	/**
	 * 获取会话的最近N条消息
	 */
	@Operation(summary = "获取最近N条消息")
	@GetMapping("/recent/{conversationId}")
	public Result<List<AiMessageDTO>> getRecentMessages(@PathVariable Long conversationId,
			@RequestParam(defaultValue = "20") int limit) {
		return Result.success(messageService.getRecentMessages(conversationId, limit));
	}

	/**
	 * 分页查询消息
	 */
	@Operation(summary = "分页查询消息")
	@GetMapping("/page")
	public Result<PageResult<AiMessageDTO>> page(MessageQueryDTO queryVO) {
		return Result.success(messageService.page(queryVO));
	}

}
