package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI会话实体
 *
 * @author fxz
 * @date 2025-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_conversations")
public class AiConversationDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	/**
	 * 会话UUID
	 */
	private String conversationUuid;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	/**
	 * 应用ID
	 */
	private Long appId;

	/**
	 * 终端用户ID
	 */
	private Long endUserId;

	/**
	 * 会话标题
	 */
	private String name;

	/**
	 * 会话状态：active-活跃, archived-已归档, deleted-已删除
	 */
	private String status;

	/**
	 * 消息总数
	 */
	private Integer messageCount;

	/**
	 * 总Token消耗
	 */
	private Integer totalTokens;

	/**
	 * 总成本
	 */
	private BigDecimal totalCost;

	/**
	 * 首条消息时间
	 */
	private LocalDateTime firstMessageAt;

	/**
	 * 最后消息时间
	 */
	private LocalDateTime lastMessageAt;

	/**
	 * 归档时间
	 */
	private LocalDateTime archivedAt;

}
