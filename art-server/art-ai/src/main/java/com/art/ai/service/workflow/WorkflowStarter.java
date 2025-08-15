package com.art.ai.service.workflow;

import com.art.ai.service.workflow.callback.Callback;
import com.art.ai.service.workflow.definition.WorkflowsService;
import com.art.ai.service.workflow.runtime.WorkflowRuntimeService;
import lombok.RequiredArgsConstructor;
import org.bsc.langgraph4j.GraphStateException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/9 19:30
 */
@RequiredArgsConstructor
@Component
public class WorkflowStarter {

	private final WorkflowRuntimeService workflowRuntimeService;

	private final WorkflowsService workflowsService;

	/**
	 * 启动工作流,流式执行
	 * @param workflowId 工作流 ID
	 * @param userInputs 用户输入参数列表
	 */
	@Transactional
	public void streaming(String workflowId, Map<String, Object> userInputs, String conversationId)
			throws GraphStateException {
		List<Callback> callbacks = new ArrayList<>();
		WorkflowEngine workflowEngine = new WorkflowEngine(workflowRuntimeService, workflowsService, callbacks,
				new Workflow(workflowId));
		workflowEngine.run(userInputs, conversationId);
	}

	/**
	 * 恢复工作流
	 * @param runtimeUuid 工作流运行时唯一标识符
	 * @param userInput 用户输入参数
	 */
	public void resumeFlow(String runtimeUuid, String userInput) {
	}

}
