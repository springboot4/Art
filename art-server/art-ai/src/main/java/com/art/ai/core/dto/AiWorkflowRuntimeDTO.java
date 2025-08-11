package com.art.ai.core.dto;

import com.art.ai.service.workflow.runtime.WorkFlowStatus;
import com.art.mybatis.common.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * @author fxz
 * @date 2025-08-09
 */
@Schema(title = "")
@Data
public class AiWorkflowRuntimeDTO extends BaseCreateEntity {

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
	private Integer status = WorkFlowStatus.WORKFLOW_PROCESS_STATUS_CREATE;

	@Schema(description = "状态描述")
	private String statusRemark;

}