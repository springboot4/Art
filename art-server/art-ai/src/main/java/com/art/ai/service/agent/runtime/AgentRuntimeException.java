package com.art.ai.service.agent.runtime;

/**
 * Agent 运行异常
 *
 * @author fxz
 * @since 2025-11-01
 */
public class AgentRuntimeException extends Exception {

	public AgentRuntimeException(String message) {
		super(message);
	}

	public AgentRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
