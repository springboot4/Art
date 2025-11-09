package com.art.ai.service.agent.runtime;

import java.util.Map;

/**
 * Agent进度回调接口 用于通知Agent执行过程中的中间状态
 *
 * @author fxz
 */
@FunctionalInterface
public interface AgentProgressCallback {

	/**
	 * 通知进度更新
	 * @param type 进度类型：plan | tool_start | tool_end
	 * @param data 进度数据
	 */
	void onProgress(String type, Map<String, Object> data);

}
