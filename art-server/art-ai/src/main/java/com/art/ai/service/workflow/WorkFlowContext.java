package com.art.ai.service.workflow;

import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.service.workflow.callback.MessageCompletionCallback;
import com.art.ai.service.workflow.variable.VariablePool;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 工作流上下文
 *
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

	/**
	 * 工作流运行时
	 */
	private AiWorkflowRuntimeDTO runtime;

	/**
	 * 消息完成回调
	 */
	private MessageCompletionCallback messageCompletionCallback;

	/**
	 * 会话变量声明
	 */
	private Map<String, Object> conversationVariableDeclaration;

}
