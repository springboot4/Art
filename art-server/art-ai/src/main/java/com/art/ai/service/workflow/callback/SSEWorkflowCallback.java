package com.art.ai.service.workflow.callback;

import com.art.ai.core.sse.SSEEmitterHelper;
import com.art.json.sdk.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

/**
 * SSE工作流回调 负责将工作流节点执行状态通过SSE推送给前端
 *
 * @author fxz
 */
@Slf4j
@RequiredArgsConstructor
public class SSEWorkflowCallback implements Callback {

	private final SseEmitter sseEmitter;

	@Override
	public void execute(CallbackResult callbackResult) {
		if (Objects.isNull(callbackResult)) {
			log.warn("CallbackResult为空，跳过处理");
			return;
		}

		CallbackData data = callbackResult.getData();
		if (Objects.isNull(data)) {
			log.warn("CallbackData为空，跳过处理");
			return;
		}

		if (data.getNodeId().equals(START)) {
			handleStartNode();
			return;
		}

		if (data.getNodeId().equals(END)) {
			handleEndNode();
			return;
		}

		handleNormalNode(data);
	}

	/**
	 * 处理START节点
	 */
	private void handleStartNode() {
		try {
			SSEEmitterHelper.sendStart(sseEmitter);
		}
		catch (Exception e) {
			log.error("发送START事件失败", e);
		}
	}

	/**
	 * 处理END节点
	 */
	private void handleEndNode() {
		try {
			SSEEmitterHelper.sendComplete(sseEmitter);
		}
		catch (Exception e) {
			log.error("发送DONE事件失败", e);
		}
	}

	/**
	 * 处理普通节点
	 */
	private void handleNormalNode(CallbackData data) {
		try {
			String jsonData = JacksonUtil.toJsonString(data);
			SSEEmitterHelper.parseAndSendPartialMsg(sseEmitter, jsonData);
		}
		catch (Exception e) {
			log.error("推送节点数据失败，nodeId={}", data.getNodeId(), e);
		}
	}

}
