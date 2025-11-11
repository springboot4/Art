package com.art.ai.service.agent.runtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * kind = tool_calls 时的模型输出。
 *
 * @author fxz
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentToolDecisionPayload extends AgentDecisionPayload {

	private List<AgentToolCallPayload> toolCalls;

}
