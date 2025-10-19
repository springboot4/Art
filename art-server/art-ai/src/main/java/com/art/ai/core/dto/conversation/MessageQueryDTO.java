package com.art.ai.core.dto.conversation;

import com.art.core.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息查询请求VO
 *
 * @author fxz
 * @date 2025-10-18
 */
@Schema(title = "消息查询请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageQueryDTO extends PageParam {

	@Schema(description = "会话ID")
	private Long conversationId;

	@Schema(description = "角色")
	private String role;

	@Schema(description = "状态")
	private String status;

}
