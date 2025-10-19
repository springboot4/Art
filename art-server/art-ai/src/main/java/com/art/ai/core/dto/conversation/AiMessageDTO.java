package com.art.ai.core.dto.conversation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI消息DTO
 *
 * @author fxz
 * @date 2025-10-18
 */
@Schema(title = "AI消息信息")
@Data
public class AiMessageDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "主键ID")
	private Long id;

	@Schema(description = "消息UUID")
	private String messageUuid;

	@Schema(description = "会话ID")
	private Long conversationId;

	@Schema(description = "实例ID")
	private Long instanceId;

	@Schema(description = "实例类型")
	private String instanceType;

	@Schema(description = "角色")
	private String role;

	@Schema(description = "消息类型：text-文本, image-图片, file-文件, audio-音频, video-视频")
	private String messageType;

	@Schema(description = "消息内容")
	private String content;

	@Schema(description = "模型提供商")
	private String modelProvider;

	@Schema(description = "模型ID")
	private String modelId;

	@Schema(description = "输入Token数")
	private Integer promptTokens;

	@Schema(description = "输出Token数")
	private Integer completionTokens;

	@Schema(description = "总Token数")
	private Integer totalTokens;

	@Schema(description = "总成本")
	private BigDecimal totalCost;

	@Schema(description = "状态")
	private String status;

	@Schema(description = "错误信息")
	private String errorMessage;

	@Schema(description = "扩展元数据")
	private String metadata;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "完成时间")
	private LocalDateTime completedAt;

}
