package com.art.ai.service.workflow;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/9 19:30
 */
public class WorkflowStarter {

	/**
	 * 启动工作流,流式执行
	 * @param workflowId 工作流 ID
	 * @param userInputs 用户输入参数列表
	 */
	public void streaming(String workflowId, List<ObjectNode> userInputs) {
	}

	/**
	 * 恢复工作流
	 * @param runtimeUuid 工作流运行时唯一标识符
	 * @param userInput 用户输入参数
	 */
	public void resumeFlow(String runtimeUuid, String userInput) {
	}

}
