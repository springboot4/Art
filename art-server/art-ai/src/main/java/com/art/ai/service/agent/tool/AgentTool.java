package com.art.ai.service.agent.tool;

/**
 * Agent 工具接口
 *
 * @author fxz
 * @since 2025-11-01
 */
public interface AgentTool {

	String name();

	AgentToolDefinition definition();

	AgentToolResult invoke(AgentToolRequest request) throws AgentToolException;

}
