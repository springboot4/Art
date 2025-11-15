package com.art.ai.service.agent.runtime;

import com.art.core.common.exception.ArtException;

/**
 * Agent 用户输入异常 当用户提供的输入变量不满足 Agent 定义要求时抛出
 *
 * @author fxz
 * @since 2025-11-15
 */
public class AgentUserInputException extends ArtException {

	public AgentUserInputException(String message) {
		super(message);
	}

}
