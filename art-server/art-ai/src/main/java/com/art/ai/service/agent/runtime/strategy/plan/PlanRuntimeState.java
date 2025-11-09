package com.art.ai.service.agent.runtime.strategy.plan;

import com.art.ai.core.dto.conversation.AiMessageDTO;
import com.art.ai.service.agent.runtime.AgentPlanItem;
import com.art.ai.service.agent.runtime.AgentPlanItemStatus;
import com.art.ai.service.agent.runtime.AgentPlanItemType;
import com.art.ai.service.agent.runtime.AgentPlanSnapshot;
import com.art.ai.service.agent.runtime.AgentPlanStatus;
import com.art.ai.service.agent.runtime.AgentStep;
import com.art.ai.service.agent.runtime.AgentToolCall;
import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.core.common.util.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plan-Execute Agent 运行时状态 包含计划管理相关的所有字段和逻辑
 *
 * @author fxz
 */
@Slf4j
@Getter
public class PlanRuntimeState {

	private final String runId;

	private final Long agentId;

	private final AgentSpec spec;

	private final String userInput;

	private final Long conversationId;

	private final List<AiMessageDTO> memory;

	private final Map<String, Object> variables;

	private final Map<String, Object> conversationVariables;

	private final VariablePool variablePool;

	private final Instant startTime;

	private final List<AgentStep> steps = new ArrayList<>();

	private final List<AgentPlanItem> plan = new ArrayList<>();

	private final Map<String, Integer> planIndexById = new HashMap<>();

	private int stepCounter = 0;

	private int toolCallCounter = 0;

	private boolean planEstablished = false;

	@Setter
	private AgentPlanStatus planStatus;

	private int activePlanIndex = 0;

	private Integer waitingPlanIndex;

	private String waitingPlanStepId;

	private String waitingMessage;

	public PlanRuntimeState(String runId, Long agentId, AgentSpec spec, String userInput, Long conversationId,
			List<AiMessageDTO> memory, Map<String, Object> variables, Map<String, Object> conversationVariables,
			Instant startTime) {
		this.runId = runId;
		this.agentId = agentId;
		this.spec = spec;
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
		planIndexById.clear();
		activePlanIndex = 0;
		waitingPlanIndex = null;
		waitingPlanStepId = null;
		waitingMessage = null;
		planStatus = AgentPlanStatus.ACTIVE;

		if (planItems != null) {
			int fallback = 1;
			for (AgentPlanItem item : planItems) {
				AgentPlanItem normalized = normalizePlanItem(item, fallback);
				plan.add(normalized);
				planIndexById.put(normalized.getStepId(), plan.size() - 1);
				fallback = Math.max(fallback, normalized.getStep() + 1);
			}
		}

		planEstablished = !plan.isEmpty();
		if (planEstablished) {
			rebuildPlanStatuses();
		}
	}

	/**
	 * 从快照恢复计划状态
	 */
	public void restorePlan(AgentPlanSnapshot snapshot) {
		if (snapshot == null) {
			return;
		}

		plan.clear();
		planIndexById.clear();

		if (snapshot.getPlan() != null) {
			int fallback = 1;
			for (AgentPlanItem item : snapshot.getPlan()) {
				AgentPlanItem normalized = normalizePlanItem(item, fallback);
				plan.add(normalized);
				planIndexById.put(normalized.getStepId(), plan.size() - 1);
				fallback = Math.max(fallback, normalized.getStep() + 1);
			}
		}

		planEstablished = !plan.isEmpty();

		planStatus = snapshot.getStatus() != null ? snapshot.getStatus()
				: (planEstablished ? AgentPlanStatus.ACTIVE : planStatus);

		activePlanIndex = clampPointer(snapshot.getActivePlanIndex());

		waitingPlanIndex = normalizeWaitingIndex(snapshot.getWaitingPlanIndex(), snapshot.getWaitingPlanStepId());

		waitingPlanStepId = waitingPlanIndex != null && waitingPlanIndex < plan.size()
				? plan.get(waitingPlanIndex).getStepId() : snapshot.getWaitingPlanStepId();

		waitingMessage = snapshot.getWaitingMessage();

		if (CollectionUtil.isNotEmpty(snapshot.getSteps())) {
			steps.clear();
			steps.addAll(snapshot.getSteps());
			stepCounter = Math.max(stepCounter, steps.size());
		}

		stepCounter = Math.max(stepCounter, Math.max(snapshot.getStepCounter(), 0));
		toolCallCounter = Math.max(toolCallCounter, Math.max(snapshot.getToolCallCounter(), 0));

		rebuildPlanStatuses();
	}

	public List<AgentStep> unmodifiableSteps() {
		return Collections.unmodifiableList(steps);
	}

	public List<AgentPlanItem> unmodifiablePlan() {
		return Collections.unmodifiableList(plan);
	}

	public AgentPlanItem getActivePlanItem() {
		if (!planEstablished || activePlanIndex < 0 || activePlanIndex >= plan.size()) {
			return null;
		}
		return plan.get(activePlanIndex);
	}

	public boolean autoAdvancePlan(List<AgentToolCall> executedCalls) {
		if (!planEstablished || activePlanIndex >= plan.size()) {
			return false;
		}

		AgentPlanItem currentStep = plan.get(activePlanIndex);
		boolean stepCompleted = isStepCompleted(currentStep, executedCalls);

		if (stepCompleted) {
			currentStep.setStatus(AgentPlanItemStatus.DONE);
			activePlanIndex++;

			if (activePlanIndex < plan.size()) {
				plan.get(activePlanIndex).setStatus(AgentPlanItemStatus.IN_PROGRESS);
			}
			else {
				planStatus = AgentPlanStatus.COMPLETED;
			}

			log.debug("计划推进: 步骤 {} 完成, 当前索引: {}", currentStep.getStep(), activePlanIndex);
			return true;
		}

		return false;
	}

	public void markCurrentStepFailed(String errorMessage) {
		if (!planEstablished || activePlanIndex >= plan.size()) {
			return;
		}

		AgentPlanItem currentStep = plan.get(activePlanIndex);
		currentStep.setStatus(AgentPlanItemStatus.FAILED);
		log.warn("计划步骤 {} 失败: {}", currentStep.getStep(), errorMessage);
	}

	public void markWaitingForUser(String message) {
		waitingMessage = message;
		if (!planEstablished || plan.isEmpty()) {
			planStatus = AgentPlanStatus.WAITING_USER;
			return;
		}

		waitingPlanIndex = Math.min(activePlanIndex, plan.size() - 1);
		AgentPlanItem current = plan.get(waitingPlanIndex);
		current.setStatus(AgentPlanItemStatus.WAITING_USER);
		waitingPlanStepId = current.getStepId();
		planStatus = AgentPlanStatus.WAITING_USER;
		rebuildPlanStatuses();
	}

	public void clearWaitingUser() {
		if (waitingPlanIndex != null && waitingPlanIndex >= 0 && waitingPlanIndex < plan.size()) {
			AgentPlanItem item = plan.get(waitingPlanIndex);
			if (item.getStatus() == AgentPlanItemStatus.WAITING_USER) {
				item.setStatus(AgentPlanItemStatus.IN_PROGRESS);
			}
		}

		waitingPlanIndex = null;
		waitingPlanStepId = null;
		waitingMessage = null;
		if (planStatus == AgentPlanStatus.WAITING_USER) {
			planStatus = AgentPlanStatus.ACTIVE;
		}

		rebuildPlanStatuses();
	}

	private boolean isStepCompleted(AgentPlanItem step, List<AgentToolCall> executedCalls) {
		if (executedCalls == null || executedCalls.isEmpty()) {
			return false;
		}

		if (StringUtils.isNotBlank(step.getTool())) {
			return executedCalls.stream().anyMatch(call -> call.getName().equals(step.getTool()));
		}

		if (step.getType() == AgentPlanItemType.TOOL) {
			return true;
		}

		return false;
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

	private AgentPlanItem normalizePlanItem(AgentPlanItem source, int defaultStep) {
		if (source == null) {
			throw new IllegalArgumentException("计划项不能为空");
		}

		int step = source.getStep() > 0 ? source.getStep() : defaultStep;
		String stepId = StringUtils.defaultIfBlank(source.getStepId(), "step-" + step);
		AgentPlanItemType type = source.getType() != null ? source.getType() : inferPlanType(source);

		AgentPlanItem normalized = source.toBuilder()
			.step(step)
			.stepId(stepId)
			.type(type)
			.requiresUser(source.getRequiresUser())
			.status(AgentPlanItemStatus.PENDING)
			.build();

		return normalized;
	}

	private AgentPlanItemType inferPlanType(AgentPlanItem item) {
		if (StringUtils.isNotBlank(item.getTool())) {
			return AgentPlanItemType.TOOL;
		}
		return AgentPlanItemType.MESSAGE;
	}

	private void rebuildPlanStatuses() {
		if (!planEstablished || plan.isEmpty()) {
			return;
		}

		for (int i = 0; i < plan.size(); i++) {
			AgentPlanItem item = plan.get(i);
			if (item.getStatus() == AgentPlanItemStatus.FAILED || item.getStatus() == AgentPlanItemStatus.SKIPPED) {
				continue;
			}

			if (i < activePlanIndex) {
				item.setStatus(AgentPlanItemStatus.DONE);
			}
			else if (Integer.valueOf(i).equals(waitingPlanIndex)) {
				item.setStatus(AgentPlanItemStatus.WAITING_USER);
			}
			else if (i == activePlanIndex) {
				item.setStatus(AgentPlanItemStatus.IN_PROGRESS);
			}
			else {
				item.setStatus(AgentPlanItemStatus.PENDING);
			}
		}

		if (activePlanIndex >= plan.size()) {
			planStatus = AgentPlanStatus.COMPLETED;
		}
	}

	private int clampPointer(Integer pointer) {
		if (pointer == null || pointer < 0) {
			return 0;
		}
		return Math.min(pointer, plan.size());
	}

	private Integer normalizeWaitingIndex(Integer waitingIndex, String waitingStepId) {
		if (waitingIndex != null && waitingIndex >= 0 && waitingIndex < plan.size()) {
			return waitingIndex;
		}

		if (StringUtils.isNotBlank(waitingStepId)) {
			return planIndexById.get(waitingStepId);
		}

		return null;
	}

}
