package com.art.ai.core.dto.conversation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新会话请求VO
 *
 * @author fxz
 * @date 2025-10-18
 */
@Schema(title = "更新会话请求")
@Data
public class ConversationUpdateDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "会话标题", required = true)
	@NotBlank(message = "会话标题不能为空")
	private String name;

	@Schema(description = "会话ID", required = true)
	@NotNull
	private Long conversationId;

}
