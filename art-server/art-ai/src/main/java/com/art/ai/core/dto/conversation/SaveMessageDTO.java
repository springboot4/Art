package com.art.ai.core.dto.conversation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 保存消息请求VO
 *
 * @author fxz
 * @date 2025-10-18
 */
@Schema(title = "保存消息请求")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMessageDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "会话ID", required = true)
	@NotNull(message = "会话ID不能为空")
	private Long conversationId;

	@Schema(description = "实例ID（预留）")
	private Long instanceId;

	@Schema(description = "实例类型（预留）")
	private String instanceType;

	@Schema(description = "角色", required = true)
	@NotBlank(message = "角色不能为空")
	private String role;

	@Schema(description = "消息类型：text-文本, image-图片, file-文件, audio-音频, video-视频（默认text）")
	private String messageType;

	@Schema(description = "消息内容", required = true)
	@NotBlank(message = "消息内容不能为空")
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

}
