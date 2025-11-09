package com.art.ai.service.agent.runtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * agent_final 控制函数的参数
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentFinalFunctionPayload implements AgentControlPayload {

	/**
	 * 面向用户的消息
	 */
	private String message;

	/**
	 * 是否需要等待用户输入（仅PLAN模式需要）
	 */
	private Boolean requiresUserInput;

}
