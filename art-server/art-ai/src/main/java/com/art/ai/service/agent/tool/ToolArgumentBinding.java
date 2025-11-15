package com.art.ai.service.agent.tool;

import com.art.ai.service.workflow.variable.VariableSelector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工具参数绑定
 *
 * @author fxz
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ToolArgumentBinding {

	private String field;

	/**
	 * 使用系统变量选择器解析值
	 */
	private VariableSelector selector;

	/**
	 * 固定值
	 */
	private Object constant;

	/**
	 * 是否强制覆盖模型提供的值
	 */
	@Builder.Default
	private boolean override = false;

}
