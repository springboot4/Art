package com.art.ai.service.workflow.exception;

import lombok.Getter;

/**
 * 工作流异常
 * <p>
 * 统一的工作流执行异常，包含错误码和描述信息
 *
 * @author fxz
 * @since 2026/02/27
 */
@Getter
public class WorkflowException extends RuntimeException {

	private final WorkflowErrorCode errorCode;

	public WorkflowException(WorkflowErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public WorkflowException(WorkflowErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * 获取完整错误信息
	 */
	public String getFullMessage() {
		return String.format("[%s] %s", errorCode.name(), getMessage());
	}

}
