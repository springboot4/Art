package com.art.ai.service.workflow.domain.node.llm;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import com.art.ai.service.workflow.domain.node.llm.config.MemoryConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 15:39
 */
@Data
public class LlmNodeConfig extends NodeConfig {

	private String model;

	private String systemPrompt;

	private List<Message> messages;

	private Double temperature = 1.0;

	private Integer maxTokens = 128;

	/**
	 * 记忆配置
	 */
	private MemoryConfig memory;

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class Message {

		private String role;

		private String content;

	}

}
