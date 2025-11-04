package com.art.ai.service.agent.runtime;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariablePool;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.EnumMap;
import java.util.List;

/**
 * Agent 运行状态
 *
 * @author fxz
 * @since 2025-11-01
 */
@Getter
public class AgentRuntimeState {

	private final String runId;

	private final AgentSpec spec;

	private final AiAgentDTO agent;

	private final String userInput;

	private final Long conversationId;

	private final List<AiMessageDTO> memory;

	private final Map<String, Object> variables;

	private final Map<String, Object> conversationVariables;

	private final VariablePool variablePool;

	private final Instant startTime;

	private final List<AgentStep> steps = new ArrayList<>();

	private final List<AgentPlanItem> plan = new ArrayList<>();

	private int stepCounter = 0;

	private int toolCallCounter = 0;

	private boolean planEstablished = false;

	public AgentRuntimeState(String runId, AgentSpec spec, AiAgentDTO agent, String userInput, Long conversationId,
			List<AiMessageDTO> memory, Map<String, Object> variables, Map<String, Object> conversationVariables,
			Instant startTime) {
		this.runId = runId;
		this.spec = spec;
		this.agent = agent;
		this.userInput = userInput;
		this.conversationId = conversationId;
		this.memory = memory == null ? Collections.emptyList() : List.copyOf(memory);

		Map<String, Object> safeVariables = variables == null ? Collections.emptyMap() : new HashMap<>(variables);
		this.variables = Collections.unmodifiableMap(safeVariables);

		Map<String, Object> safeConversationVars = conversationVariables == null ? Collections.emptyMap()
				: new HashMap<>(conversationVariables);
		this.conversationVariables = Collections.unmodifiableMap(safeConversationVars);

		this.variablePool = buildVariablePool(userInput, conversationId, safeConversationVars, safeVariables);

		this.startTime = startTime;
	}

	public void addStep(AgentStep step) {
		steps.add(step);
		stepCounter++;
		if (step.getToolCalls() != null) {
			toolCallCounter += step.getToolCalls().size();
		}
	}

	public void recordPlan(List<AgentPlanItem> planItems) {
		plan.clear();
		if (planItems != null) {
			plan.addAll(planItems);
		}
		planEstablished = !plan.isEmpty();
	}

	public List<AgentStep> unmodifiableSteps() {
		return Collections.unmodifiableList(steps);
	}

	public List<AgentPlanItem> unmodifiablePlan() {
		return Collections.unmodifiableList(plan);
	}

	private VariablePool buildVariablePool(String userInput, Long conversationId, Map<String, Object> conversationVars,
			Map<String, Object> userInputs) {
		EnumMap<SystemVariableKey, Object> systemVars = new EnumMap<>(SystemVariableKey.class);
		if (conversationId != null) {
			systemVars.put(SystemVariableKey.CONVERSATION_ID, conversationId);
		}
		if (userInput != null) {
			systemVars.put(SystemVariableKey.QUERY, userInput);
		}
		return VariablePool.create(systemVars, Collections.emptyMap(), conversationVars, userInputs);
	}

}
