package com.art.ai.service.agent.runtime.dto;

import com.art.ai.service.agent.runtime.AgentDecision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础决策载荷，根据 kind 不同由具体子类承载附加信息。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "kind",
		visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = AgentPlanDecisionPayload.class, name = AgentDecisionPayload.KIND_PLAN),
		@JsonSubTypes.Type(value = AgentToolDecisionPayload.class, name = AgentDecisionPayload.KIND_TOOL_CALLS),
		@JsonSubTypes.Type(value = AgentFinalDecisionPayload.class, name = AgentDecisionPayload.KIND_FINAL) })
public abstract class AgentDecisionPayload {

	public static final String KIND_PLAN = "plan";

	public static final String KIND_TOOL_CALLS = "tool_calls";

	public static final String KIND_FINAL = "final";

	/**
	 * 决策类型：tool_calls | final | plan
	 */
	private String kind;

	public AgentDecision.DecisionKind mapKind() {
		String normalized = StringUtils.defaultString(kind, "tool_calls").toLowerCase();
		return switch (normalized) {
			case "final" -> AgentDecision.DecisionKind.FINAL;
			case "plan" -> AgentDecision.DecisionKind.PLAN;
			case "error" -> AgentDecision.DecisionKind.ERROR;
			default -> AgentDecision.DecisionKind.TOOL_CALLS;
		};
	}

}
