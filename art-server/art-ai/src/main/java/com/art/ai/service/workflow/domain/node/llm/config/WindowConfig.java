package com.art.ai.service.workflow.domain.node.llm.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记忆窗口配置
 *
 * @author fxz
 * @date 2025-10-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WindowConfig {

	/**
	 * 窗口大小（历史消息条数，默认10条）
	 */
	private Integer size = 10;

}
