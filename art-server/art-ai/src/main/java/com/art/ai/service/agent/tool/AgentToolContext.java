package com.art.ai.service.agent.tool;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * 工具执行上下文
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentToolContext {

	private final Long conversationId;

	private final String conversationUuid;

	private final String runId;

	private final String language;

	@Builder.Default
	private final Map<String, Object> extras = Collections.emptyMap();

}
