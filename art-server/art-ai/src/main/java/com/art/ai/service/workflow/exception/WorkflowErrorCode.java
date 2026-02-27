package com.art.ai.service.workflow.exception;

import lombok.Getter;

/**
 * 工作流错误码枚举
 *
 * @author fxz
 * @since 2026/02/27
 */
@Getter
public enum WorkflowErrorCode {

	/**
	 * 工作流定义不存在
	 */
	WORKFLOW_NOT_FOUND("工作流定义不存在"),

	/**
	 * 图构建失败
	 */
	GRAPH_BUILD_FAILED("图构建失败"),

	/**
	 * 节点执行失败
	 */
	NODE_EXECUTION_FAILED("节点执行失败"),

	/**
	 * 执行超时
	 */
	EXECUTION_TIMEOUT("执行超时"),

	/**
	 * 持久化失败
	 */
	PERSISTENCE_FAILED("持久化失败");

	private final String description;

	WorkflowErrorCode(String description) {
		this.description = description;
	}

}
