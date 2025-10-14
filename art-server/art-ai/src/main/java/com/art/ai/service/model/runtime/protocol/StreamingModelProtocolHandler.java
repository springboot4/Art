package com.art.ai.service.model.runtime.protocol;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import dev.langchain4j.model.chat.StreamingChatModel;

/**
 * Streaming 模型协议处理器
 *
 * <p>
 * 用于在运行时根据模型协议创建 {@link StreamingChatModel}。
 * </p>
 *
 * @author fxz
 */
public interface StreamingModelProtocolHandler {

	/**
	 * 判断当前处理器是否支持给定能力的上下文。
	 * @param context 模型运行时上下文
	 * @param capability 模型能力
	 * @return 是否支持
	 */
	boolean supports(AiModelRuntimeContext context, AiModelCapability capability);

	/**
	 * 创建流式聊天模型。
	 * @param context 模型运行时上下文
	 * @param options 调用参数
	 * @return {@link StreamingChatModel}
	 */
	StreamingChatModel createStreamingChatModel(AiModelRuntimeContext context, AiModelInvokeOptions options);

}
