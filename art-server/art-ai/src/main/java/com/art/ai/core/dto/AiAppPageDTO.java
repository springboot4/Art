package com.art.ai.core.dto;

import com.art.core.common.model.BasePageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fxz
 * @date 2025-07-25
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "")
@Data
public class AiAppPageDTO extends BasePageEntity {

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