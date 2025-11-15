package com.art.ai.service.agent.runtime.strategy.react;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.service.agent.runtime.AgentStep;
import com.art.ai.service.agent.runtime.AgentToolCall;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariablePool;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REACT Agent 运行时状态 纯粹的ReAct模式，无任何plan相关字段
 *
 * @author fxz
 */
@Getter
public class ReactRuntimeState {

	private final String runId;

	private final AgentSpec spec;

	private final String userInput;

	private final Long conversationId;

	private final List<AiMessageDTO> memory;

	private final Map<String, Object> variables;

	private final Map<String, Object> conversationVariables;

	private final VariablePool variablePool;

	private final Instant startTime;

	private final List<AgentStep> steps = new ArrayList<>();

	/**
	 * 执行步数
	 */
	private int stepCounter = 0;

	/**
	 * 工具调用次数
	 */
	private int toolCallCounter = 0;

	public ReactRuntimeState(String runId, AgentSpec spec, String userInput, Long conversationId,
			List<AiMessageDTO> memory, VariablePool variablePool, Instant startTime) {
		this.runId = runId;
		this.spec = spec;
		this.userInput = userInput;
		this.conversationId = conversationId;
		this.memory = memory == null ? Collections.emptyList() : List.copyOf(memory);
		this.variablePool = variablePool;
		this.startTime = startTime;
	}

	public void addStep(AgentStep step) {
		steps.add(step);
		stepCounter++;
		if (step.getToolCalls() != null) {
			toolCallCounter += step.getToolCalls().size();
		}
	}

	public List<AgentStep> unmodifiableSteps() {
		return Collections.unmodifiableList(steps);
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
