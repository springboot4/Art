package com.art.ai.service.workflow.domain.node.llm.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 大模型节点记忆配置
 *
 * @author fxz
 * @date 2025-10-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryConfig {

	/**
	 * 是否启用记忆
	 */
	private Boolean enabled = false;

	/**
	 * 窗口配置
	 */
	private WindowConfig window = new WindowConfig();

}
