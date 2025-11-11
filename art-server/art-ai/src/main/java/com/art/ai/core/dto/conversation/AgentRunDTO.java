package com.art.ai.core.dto.conversation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Agent 运行请求 DTO
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
public class AgentRunDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "Agent ID", required = true)
	private Long agentId;

	@Schema(description = "运行 ID", required = false)
	private String runId;

	@Schema(description = "会话 ID", required = true)
	private Long conversationId;

	@Schema(description = "用户输入", required = true)
	private String userQuery;

	@Schema(description = "运行变量")
	private Map<String, Object> variables;

}
