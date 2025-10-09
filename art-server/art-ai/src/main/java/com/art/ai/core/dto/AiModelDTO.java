package com.art.ai.core.dto;

import com.art.mybatis.common.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author fxz
 * @date 2025-10-09
 */
@Schema(title = "")
@Data
public class AiModelDTO extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "模型类型（推理、chat等）")
	private String type;

	@Schema(description = "模型名称")
	private String name;

	@Schema(description = "所属平台")
	private Long platform;

	@Schema(description = "是否启用")
	private Integer enable;

	@Schema(description = "最大输入长度")
	private Long maxInputTokens;

	@Schema(description = "最大输出长度")
	private Long maxOutputTokens;

	@Schema(description = "模型配置")
	private String config;

	@Schema(description = "")
	private Long tenantId;

}