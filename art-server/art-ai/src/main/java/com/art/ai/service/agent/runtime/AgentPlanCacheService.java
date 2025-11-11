package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.runtime.strategy.plan.PlanRuntimeState;
import com.art.core.common.util.CollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 负责在 Redis 中缓存 Agent 生成的计划，以便跨 Run 恢复。
 *
 * @author fxz
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AgentPlanCacheService {

	private static final Duration TTL = Duration.ofMinutes(10);

	private static final String KEY_PREFIX = "agent:plan:";

	private final StringRedisTemplate stringRedisTemplate;

	private final ObjectMapper objectMapper;

	public Optional<AgentPlanSnapshot> load(String runId, Long agentId) {
		if (StringUtils.isBlank(runId)) {
			return Optional.empty();
		}

		String key = buildKey(runId);
		String payloadJson = stringRedisTemplate.opsForValue().get(key);
		if (StringUtils.isBlank(payloadJson)) {
			return Optional.empty();
		}

		try {
			AgentPlanPayload payload = objectMapper.readValue(payloadJson, AgentPlanPayload.class);
			if (payload == null || CollectionUtil.isEmpty(payload.getPlan())
					|| !Objects.equals(payload.getAgentId(), agentId)) {
				stringRedisTemplate.delete(key);
				return Optional.empty();
			}

			payload.setStatus(AgentPlanStatus.ACTIVE);
			payload.setUpdatedAt(Instant.now().toEpochMilli());
			writePayload(key, payload);
			return Optional.of(toSnapshot(payload));
		}
		catch (JsonProcessingException ex) {
			log.warn("Failed to parse plan cache {}, clearing", key, ex);
			stringRedisTemplate.delete(key);
			return Optional.empty();
		}
	}

	/**
	 * 持久化计划状态
	 */
	public void persist(PlanRuntimeState state, AgentPlanStatus status) {
		if (state == null || state.unmodifiablePlan().isEmpty()) {
			return;
		}

		persist(buildPayloadFromPlanState(state, status));
	}

	/**
	 * 标记等待用户
	 */
	public void markWaiting(PlanRuntimeState state) {
		if (state == null || state.unmodifiablePlan().isEmpty()) {
			return;
		}

		persist(buildPayloadFromPlanState(state, AgentPlanStatus.WAITING_USER));
	}

	public void clear(String runId) {
		if (StringUtils.isBlank(runId)) {
			return;
		}
		stringRedisTemplate.delete(buildKey(runId));
	}

	private void persist(AgentPlanPayload payload) {
		if (payload == null || payload.getPlan() == null || payload.getPlan().isEmpty()) {
			return;
		}
		payload.setUpdatedAt(Instant.now().toEpochMilli());
		writePayload(buildKey(payload.getRunId()), payload);
	}

	private void writePayload(String key, AgentPlanPayload payload) {
		try {
			String json = objectMapper.writeValueAsString(payload);
			stringRedisTemplate.opsForValue().set(key, json, TTL);
		}
		catch (JsonProcessingException ex) {
			log.error("Unable to serialize plan payload {}", payload.getRunId(), ex);
		}
		catch (DataAccessException ex) {
			log.error("Failed to write plan payload to Redis for {}", payload.getRunId(), ex);
		}
	}

	/**
	 * 从PlanRuntimeState构建缓存载荷
	 */
	private AgentPlanPayload buildPayloadFromPlanState(PlanRuntimeState state, AgentPlanStatus status) {
		AgentPlanPayload payload = new AgentPlanPayload();
		payload.setRunId(state.getRunId());
		payload.setAgentId(state.getAgentId());
		payload.setConversationId(state.getConversationId());
		payload.setPlan(toPlanPayload(state.unmodifiablePlan()));
		payload.setStatus(status != null ? status : AgentPlanStatus.ACTIVE);
		payload.setStepCounter(state.getStepCounter());
		payload.setToolCallCounter(state.getToolCallCounter());
		payload.setActivePlanIndex(state.getActivePlanIndex());
		payload.setWaitingPlanIndex(state.getWaitingPlanIndex());
		payload.setWaitingPlanStepId(state.getWaitingPlanStepId());
		payload.setWaitingMessage(state.getWaitingMessage());
		return payload;
	}

	// Redis 载荷转换回业务快照，恢复执行上下文
	private AgentPlanSnapshot toSnapshot(AgentPlanPayload payload) {
		return AgentPlanSnapshot.builder()
			.runId(payload.getRunId())
			.agentId(payload.getAgentId())
			.conversationId(payload.getConversationId())
			.plan(fromPlanPayload(payload.getPlan()))
			.stepCounter(payload.getStepCounter() != null ? payload.getStepCounter() : 0)
			.toolCallCounter(payload.getToolCallCounter() != null ? payload.getToolCallCounter() : 0)
			.status(payload.getStatus() != null ? payload.getStatus() : AgentPlanStatus.ACTIVE)
			.updatedAt(payload.getUpdatedAt() != null ? Instant.ofEpochMilli(payload.getUpdatedAt()) : Instant.now())
			.activePlanIndex(payload.getActivePlanIndex() != null ? payload.getActivePlanIndex() : 0)
			.waitingPlanIndex(payload.getWaitingPlanIndex())
			.waitingPlanStepId(payload.getWaitingPlanStepId())
			.waitingMessage(payload.getWaitingMessage())
			.build();
	}

	// 从缓存结构恢复 PlanItem，保留步骤状态信息
	private List<AgentPlanItem> fromPlanPayload(List<AgentPlanPayload.PlanItem> planItems) {
		if (CollectionUtil.isEmpty(planItems)) {
			return Collections.emptyList();
		}

		List<AgentPlanItem> plan = new ArrayList<>(planItems.size());
		for (AgentPlanPayload.PlanItem item : planItems) {
			AgentPlanItem.AgentPlanItemBuilder builder = AgentPlanItem.builder()
				.step(item.getStep() != null ? item.getStep() : plan.size() + 1)
				.stepId(item.getStepId())
				.goal(item.getGoal())
				.tool(item.getTool())
				.requiresUser(item.getRequiresUser())
				.type(item.getType() != null ? item.getType() : AgentPlanItemType.UNKNOWN)
				.status(item.getStatus() != null ? item.getStatus() : AgentPlanItemStatus.PENDING);
			plan.add(builder.build());
		}

		return plan;
	}

	// 将内部 PlanItem 写入缓存结构，序列化必要元数据
	private List<AgentPlanPayload.PlanItem> toPlanPayload(List<AgentPlanItem> planItems) {
		if (planItems == null || planItems.isEmpty()) {
			return Collections.emptyList();
		}

		List<AgentPlanPayload.PlanItem> payload = new ArrayList<>(planItems.size());
		for (AgentPlanItem item : planItems) {
			AgentPlanPayload.PlanItem planItem = new AgentPlanPayload.PlanItem();
			planItem.setStep(item.getStep());
			planItem.setStepId(item.getStepId());
			planItem.setGoal(item.getGoal());
			planItem.setTool(item.getTool());
			planItem.setType(item.getType());
			planItem.setStatus(item.getStatus());
			planItem.setRequiresUser(item.getRequiresUser());
			payload.add(planItem);
		}
		return payload;
	}

	private String buildKey(String runId) {
		return KEY_PREFIX + runId;
	}

	@Getter
	@Setter
	private static class AgentPlanPayload {

		private String runId;

		private Long agentId;

		private Long conversationId;

		private List<PlanItem> plan;

		private AgentPlanStatus status;

		private Integer stepCounter;

		private Integer toolCallCounter;

		private Long updatedAt;

		private Integer activePlanIndex;

		private Integer waitingPlanIndex;

		private String waitingPlanStepId;

		private String waitingMessage;

		@Getter
		@Setter
		private static class PlanItem {

			private Integer step;

			private String stepId;

			private String goal;

			private String tool;

			private AgentPlanItemType type;

			private AgentPlanItemStatus status;

			private Boolean requiresUser;

		}

	}

}
