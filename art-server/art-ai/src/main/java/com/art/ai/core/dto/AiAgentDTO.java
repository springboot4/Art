package com.art.ai.core.dto;

import com.art.mybatis.common.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Agent DTO
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Schema(title = "Agent配置")
@EqualsAndHashCode(callSuper = true)
public class AiAgentDTO extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "应用ID")
	private Long appId;

	@Schema(description = "Agent名称")
	private String name;

	@Schema(description = "Agent状态(draft/published)")
	private String status;

	@Schema(description = "Agent规格JSON")
	private String specJson;

	@Schema(description = "租户ID")
	private Long tenantId;

}
