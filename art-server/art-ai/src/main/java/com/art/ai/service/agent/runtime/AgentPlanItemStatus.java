package com.art.ai.service.agent.runtime;

/**
 * 单个计划条目的执行状态。
 *
 * @author fxz
 */
public enum AgentPlanItemStatus {

	PENDING,

	IN_PROGRESS,

	WAITING_USER,

	DONE,

	FAILED,

	SKIPPED

}
