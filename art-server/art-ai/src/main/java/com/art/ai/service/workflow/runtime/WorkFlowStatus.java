package com.art.ai.service.workflow.runtime;

/**
 * @author fxz
 * @since 2025/8/10 18:14
 */
public interface WorkFlowStatus {

	/**
	 * 工作流创建
	 */
	int WORKFLOW_PROCESS_STATUS_CREATE = 1;

	/**
	 * 工作流执行中
	 */
	int WORKFLOW_PROCESS_STATUS_DOING = 2;

	/**
	 * 工作流执行成功
	 */
	int WORKFLOW_PROCESS_STATUS_SUCCESS = 3;

	/**
	 * 工作流执行失败
	 */
	int WORKFLOW_PROCESS_STATUS_FAIL = 4;

	/**
	 * 工作流等待输入
	 */
	int WORKFLOW_PROCESS_STATUS_WAITING_INPUT = 5;

}
