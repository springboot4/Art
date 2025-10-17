package com.art.ai.service.workflow;

import com.art.ai.core.sse.SSEEmitterHelper;
import com.art.ai.service.workflow.callback.Callback;
import com.art.ai.service.workflow.callback.CallbackData;
import com.art.ai.service.workflow.definition.WorkflowsService;
import com.art.ai.service.workflow.runtime.WorkflowRuntimeService;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.common.tenant.context.TenantContextHolder;
import com.art.core.common.util.AsyncUtil;
import com.art.json.sdk.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

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
	public SseEmitter streaming(String workflowId, Map<String, Object> userInputs, Map<String, Object> systems) {
		SseEmitter sseEmitter = new SseEmitter(150000L);

		Callback callback = callbackResult -> {
			if (Objects.isNull(callbackResult)) {
				return;
			}
			CallbackData data = callbackResult.getData();
			if (Objects.isNull(data)) {
				return;
			}
			if (data.getNodeId().equals(START)) {
				SSEEmitterHelper.sendStart(sseEmitter);
				return;
			}
			if (data.getNodeId().equals(END)) {
				SSEEmitterHelper.sendComplete(sseEmitter);
				return;
			}

			SSEEmitterHelper.parseAndSendPartialMsg(sseEmitter, JacksonUtil.toJsonString(data));
		};

		WorkflowEngine workflowEngine = new WorkflowEngine(workflowRuntimeService, workflowsService, List.of(callback),
				new Workflow(workflowId));

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

	/**
	 * 恢复工作流
	 * @param runtimeUuid 工作流运行时唯一标识符
	 * @param userInput 用户输入参数
	 */
	public void resumeFlow(String runtimeUuid, String userInput) {
	}

}
