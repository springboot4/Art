package com.art.ai.service.workflow;

import com.art.ai.core.sse.SSEEmitterHelper;
import com.art.ai.service.conversation.variable.ConversationVariableService;
import com.art.ai.service.workflow.callback.DefaultMessageCompletionCallback;
import com.art.ai.service.workflow.callback.SSEWorkflowCallback;
import com.art.ai.service.workflow.definition.WorkflowsService;
import com.art.ai.service.workflow.runtime.WorkflowRuntimeService;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.common.tenant.context.TenantContextHolder;
import com.art.core.common.util.AsyncUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * 工作流启动器
 *
 * @author fxz
 * @since 2025/8/9 19:30
 */
@RequiredArgsConstructor
@Component
public class WorkflowStarter {

	private final WorkflowRuntimeService workflowRuntimeService;

	private final WorkflowsService workflowsService;

	private final ConversationVariableService conversationVariableService;

	/**
	 * 启动工作流,流式执行
	 * @param workflowId 工作流 ID
	 * @param userInputs 用户输入参数列表
	 */
	@Transactional
	public SseEmitter streaming(String workflowId, Map<String, Object> userInputs, Map<String, Object> systems) {
		SseEmitter sseEmitter = new SseEmitter(150000L);

		WorkflowEngine workflowEngine = new WorkflowEngine(workflowRuntimeService, workflowsService,
				conversationVariableService, List.of(new SSEWorkflowCallback(sseEmitter)),
				new DefaultMessageCompletionCallback(), new Workflow(workflowId));

		Long tenantId = TenantContextHolder.getTenantId();
		Authentication authentication = SecurityUtil.getAuthentication();
		AsyncUtil.run(() -> {
			try {
				TenantContextHolder.setTenantId(tenantId);
				SecurityUtil.setAuthentication(authentication);

				workflowEngine.run(userInputs, systems);
			}
			catch (Exception e) {
				SSEEmitterHelper.sendComplete(sseEmitter);
			}
			finally {
				TenantContextHolder.clear();
				SecurityUtil.setAuthentication(null);
			}
		});

		return sseEmitter;
	}

}
