package com.art.ai.core.dto;

import com.art.core.common.model.BasePageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent 分页查询 DTO
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Agent分页查询")
public class AiAgentPageDTO extends BasePageEntity {

	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "应用ID")
	private Long appId;

	@Schema(description = "Agent名称")
	private String name;

	@Schema(description = "状态")
	private String status;

	@Schema(description = "租户ID")
	private Long tenantId;

}
