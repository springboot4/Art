package com.art.ai.service.agent.runtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author fxz
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentPlanFunctionPayload implements AgentControlPayload {

	private List<AgentPlanItemPayload> items;

}
