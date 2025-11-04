package com.art.ai.service.agent.spec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Agent 版本生成器
 *
 * @author fxz
 * @since 2025-11-01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AgentVersionGenerator {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

	public static String newVersion() {
		return LocalDateTime.now().format(FORMATTER);
	}

}
