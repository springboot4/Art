package com.art.ai.core.dto.conversation;

import com.art.mybatis.common.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI会话DTO
 *
 * @author fxz
 * @date 2025-10-18
 */
@Schema(title = "AI会话信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class AiConversationDTO extends BaseCreateEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "主键ID")
	private Long id;

	@Schema(description = "会话UUID")
	private String conversationUuid;

	@Schema(description = "租户ID")
	private Long tenantId;

	@Schema(description = "应用ID")
	private Long appId;

	@Schema(description = "终端用户ID")
	private Long endUserId;

	@Schema(description = "会话标题")
	private String name;

	@Schema(description = "会话状态")
	private String status;

	@Schema(description = "消息总数")
	private Integer messageCount;

	@Schema(description = "总Token消耗")
	private Integer totalTokens;

	@Schema(description = "总成本")
	private BigDecimal totalCost;

	@Schema(description = "首条消息时间")
	private LocalDateTime firstMessageAt;

	@Schema(description = "最后消息时间")
	private LocalDateTime lastMessageAt;

	@Schema(description = "归档时间")
	private LocalDateTime archivedAt;

}
