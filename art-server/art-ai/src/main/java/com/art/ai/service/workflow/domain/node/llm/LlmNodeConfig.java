package com.art.ai.service.workflow.domain.node.llm;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

/**
 * @author fxz
 * @since 2025/8/10 15:39
 */
@Data
public class LlmNodeConfig extends NodeConfig {

	private String model;

	private Double temperature;

	private Integer maxTokens;

	private String systemPrompt;

	private String userMessage;

}
