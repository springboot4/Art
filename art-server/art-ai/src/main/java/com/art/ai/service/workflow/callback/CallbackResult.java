package com.art.ai.service.workflow.callback;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fxz
 */
@Builder
@Getter
@Setter
public class CallbackResult {

	/**
	 * 回调数据 具体的回调数据内容
	 */
	private CallbackData data;

}