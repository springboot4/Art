package com.art.ai.core.dto.conversation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建会话请求VO
 *
 * @author fxz
 * @date 2025-10-18
 */
@Schema(title = "创建会话请求")
@Data
public class ConversationCreateDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "应用ID", required = true)
	@NotNull(message = "应用ID不能为空")
	private Long appId;

	@Schema(description = "会话标题")
	private String name;

}
