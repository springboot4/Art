package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;

/**
 * @author fxz
 * @date 2025-08-09
 */
@Data
@TableName("ai_workflow_runtime")
public class AiWorkflowRuntimeDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 主键 */
	private Long id;

	/** 应用id */
	private Long appId;

	/** 工作流主键 */
	private Long workflowId;

	/** 流程入参 */
	private String input;

	/** 流程出参 */
	private String output;

	/** 执行状态 */
	private Integer status;

	/** 状态描述 */
	private String statusRemark;

}
