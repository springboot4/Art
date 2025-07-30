package com.art.ai.core.dto;

import com.art.core.common.model.BasePageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fxz
 * @date 2025-07-31
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "")
@Data
public class AiWorkflowsPageDTO extends BasePageEntity {

	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "应用id")
	private Long appId;

	@Schema(description = "流程类型")
	private String type;

	@Schema(description = "流程版本")
	private String version;

	@Schema(description = "图信息")
	private String graph;

	@Schema(description = "流程配置")
	private String features;

	@Schema(description = "环境变量")
	private String environmentVariables;

	@Schema(description = "会话变量")
	private String conversationVariables;

	@Schema(description = "租户id")
	private Long tenantId;

}