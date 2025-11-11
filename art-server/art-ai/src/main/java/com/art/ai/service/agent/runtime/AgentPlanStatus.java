package com.art.ai.service.agent.runtime;

/**
 * 计划整体生命周期状态。
 *
 * @author fxz
 */
public enum AgentPlanStatus {

	IDLE,

	PLANNING,

	ACTIVE,

	WAITING_USER,

	REPLAN_REQUESTED,

	HALTED,

	FAILED,

	COMPLETED

}
