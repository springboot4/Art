package com.art.ai.service.agent.runtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * kind = plan 时的模型输出。
 *
 * @author fxz
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentPlanDecisionPayload extends AgentDecisionPayload {

	private List<AgentPlanItemPayload> plan;

}
