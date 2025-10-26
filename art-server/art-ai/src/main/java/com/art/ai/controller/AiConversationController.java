package com.art.ai.controller;

import com.art.ai.core.dto.conversation.AiConversationDTO;
import com.art.ai.core.dto.conversation.ConversationCreateDTO;
import com.art.ai.core.dto.conversation.ConversationQueryDTO;
import com.art.ai.core.dto.conversation.ConversationUpdateDTO;
import com.art.ai.service.conversation.ConversationService;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI会话管理Controller
 *
 * @author fxz
 * @date 2025-10-18
 */
@Tag(name = "AI会话管理")
@RestController
@RequestMapping("/ai/conversation")
@RequiredArgsConstructor
public class AiConversationController {

	private final ConversationService conversationService;

	/**
	 * 创建会话
	 */
	@Operation(summary = "创建会话")
	@PostMapping("/create")
	public Result<AiConversationDTO> create(@Validated @RequestBody ConversationCreateDTO vo) {
		return Result.success(conversationService.create(vo));
	}

	/**
	 * 分页查询会话列表
	 */
	@Operation(summary = "分页查询会话列表")
	@GetMapping("/page")
	public Result<PageResult<AiConversationDTO>> page(ConversationQueryDTO queryVO) {
		return Result.success(conversationService.page(queryVO));
	}

	/**
	 * 更新会话标题
	 */
	@Operation(summary = "更新会话标题")
	@PostMapping("/name")
	public Result<Void> updateName(@Validated @RequestBody ConversationUpdateDTO vo) {
		conversationService.updateName(vo.getConversationId(), vo.getName());
		return Result.success();
	}

}
