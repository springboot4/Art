package com.art.ai.service.agent.runtime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Agent进度消息
 *
 * @author fxz
 */
@Getter
public class AgentProgressMessage {

	private final IntermediateProcess intermediateProcess;

	private AgentProgressMessage(String type, Map<String, Object> data) {
		this.intermediateProcess = new IntermediateProcess(type, data);
	}

	/**
	 * 创建进度消息
	 * @param type 进度类型：plan | tool_start | tool_end
	 * @param data 进度数据
	 */
	public static AgentProgressMessage of(String type, Map<String, Object> data) {
		return new AgentProgressMessage(type, data);
	}

	/**
	 * 中间过程数据
	 */
	@Getter
	@AllArgsConstructor
	public static class IntermediateProcess {

		private final String type;

		private final Map<String, Object> data;

	}

}
