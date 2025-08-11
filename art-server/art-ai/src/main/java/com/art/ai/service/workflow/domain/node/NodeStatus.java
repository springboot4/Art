package com.art.ai.service.workflow.domain.node;

/**
 * @author fxz
 * @since 2025/8/11 13:06
 */
public interface NodeStatus {

	/**
	 * 新建
	 */
	String NODE_STATUS_NEW = "new";

	/**
	 * 运行中
	 */
	String NODE_STATUS_RUNNING = "running";

	/**
	 * 成功
	 */
	String NODE_STATUS_SUCCESS = "success";

	/**
	 * 失败
	 */
	String NODE_STATUS_FAILED = "failed";

}
