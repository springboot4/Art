package com.art.ai.service.agent.runtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * kind = final 时的模型输出。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentFinalDecisionPayload extends AgentDecisionPayload {

	/**
	 * 面向用户的消息
	 */
	private String message;

	/**
	 * 是否需要等待用户输入（仅PLAN模式需要）
	 */
	private Boolean requiresUserInput;

}
