package com.art.ai.service.agent.runtime;

import lombok.Builder;
import lombok.Data;

/**
 * Agent 计划条目
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentPlanItem {

	private final int step;

	private final String goal;

	private final String tool;

}
