package com.art.ai.service.agent.runtime;

import lombok.Builder;
import lombok.Data;

/**
 * Agent 计划条目
 *
 * 包含步骤的类型、状态以及执行所需的附加信息。
 *
 * @author fxz
 */
@Data
@Builder(toBuilder = true)
public class AgentPlanItem {

	private int step;

	private String stepId;

	private String goal;

	private String tool;

	private Boolean requiresUser;

	@Builder.Default
	private AgentPlanItemType type = AgentPlanItemType.UNKNOWN;

	@Builder.Default
	private AgentPlanItemStatus status = AgentPlanItemStatus.PENDING;

}
