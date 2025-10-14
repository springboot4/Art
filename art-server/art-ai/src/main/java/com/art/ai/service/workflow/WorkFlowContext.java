package com.art.ai.service.workflow;

import com.art.ai.service.workflow.variable.VariablePool;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author fxz
 * @since 2025/8/10 17:11
 */
@Accessors(chain = true)
@Data
public class WorkFlowContext {

	private VariablePool pool;

	/**
	 * 当前运行节点 ID
	 */
	private String currentNodeId;

	/**
	 * 当前运行节点名称
	 */
	private String currentNodeLabel;

}
