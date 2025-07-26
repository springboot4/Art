package com.art.ai.core.dto;

import com.art.mybatis.common.base.BaseCreateEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

/**
 * @author fxz
 * @date 2025-07-25
 */
@Schema(title = "")
@Data
public class AiAppDTO extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	@Schema(description = "")
	private Long id;

	@Schema(description = "")
	private String name;

	@Schema(description = "")
	private String description;

	@Schema(description = "")
	private String mode;

	@Schema(description = "")
	private String icon;

	@Schema(description = "")
	private String status;

	@Schema(description = "")
	private Long tenantId;

}