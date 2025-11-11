package com.art.ai.service.agent.tool;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Agent 工具注册表
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AgentToolRegistry {

	private final List<AgentTool> toolBeans;

	private final Map<String, AgentTool> registry = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		toolBeans.forEach(this::register);
		log.info("Agent 工具注册完成，数量={}", registry.size());
	}

	public void register(AgentTool tool) {
		if (tool == null || tool.name() == null) {
			return;
		}
		registry.put(tool.name(), tool);
	}

	public AgentTool resolve(String name) {
		return registry.get(name);
	}

	public boolean contains(String name) {
		return registry.containsKey(name);
	}

}
