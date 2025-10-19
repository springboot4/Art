package com.art.ai.core.dto.conversation;

import com.art.core.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会话查询请求VO
 *
 * @author fxz
 * @date 2025-10-18
 */
@Schema(title = "会话查询请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConversationQueryDTO extends PageParam {

	@Schema(description = "应用ID")
	private Long appId;

	@Schema(description = "终端用户ID")
	private Long endUserId;

	@Schema(description = "会话状态")
	private String status;

	@Schema(description = "会话标题（模糊查询）")
	private String name;

}
