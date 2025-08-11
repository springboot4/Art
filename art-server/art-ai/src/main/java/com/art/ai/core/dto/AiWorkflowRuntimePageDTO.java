package com.art.ai.core.dto;

import com.art.core.common.model.BasePageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @author fxz
 * @date 2025-08-09
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "")
@Data
public class AiWorkflowRuntimePageDTO extends BasePageEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "应用id")
	private Long appId;

	@Schema(description = "工作流主键")
	private Long workflowId;

	@Schema(description = "流程入参")
	private String input;

	@Schema(description = "流程出参")
	private String output;

	@Schema(description = "执行状态")
	private Integer status;

	@Schema(description = "状态描述")
	private String statusRemark;

}