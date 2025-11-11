package com.art.ai.service.agent.runtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author fxz
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentPlanItemPayload {

	private Integer step;

	private String stepId;

	private String goal;

	private String tool;

	private String type;

	private Boolean requiresUser;

}
