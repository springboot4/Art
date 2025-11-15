package com.art.ai.service.agent.runtime;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Redis 缓存中计划对象的快照表示，用于在内存与持久层之间传递。
 *
 * @author fxz
 */
@Data
@Builder
public class AgentPlanSnapshot {

	private final String runId;

	private final Long agentId;

	private final Long conversationId;

	@Builder.Default
	private final List<AgentPlanItem> plan = Collections.emptyList();

	@Builder.Default
	private final List<AgentStep> steps = Collections.emptyList();

	private final int stepCounter;

	private final int toolCallCounter;

	private final AgentPlanStatus status;

	private final Instant updatedAt;

	// 当前执行指针，表示已完成步骤数量
	private final int activePlanIndex;

	// 如果存在等待用户的步骤，则保留其索引与标识
	private final Integer waitingPlanIndex;

	private final String waitingPlanStepId;

	// 等待用户时的提示语句
	private final String waitingMessage;

}
