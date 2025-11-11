package com.art.ai.service.agent.tool;

import lombok.Builder;
import lombok.Data;

/**
 * 工具执行结果
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
public class AgentToolResult {

	@Builder.Default
	private final boolean ok = true;

	private final Object data;

	private final String errorCode;

	private final String errorMessage;

	@Builder.Default
	private final boolean truncated = false;

	private final String preview;

	private final long elapsedMs;

}
