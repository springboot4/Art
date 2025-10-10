package com.art.ai.service.model.runtime.protocol;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

/**
 * 模型协议处理器
 *
 * @author fxz
 */
public interface ModelProtocolHandler {

	boolean supports(AiModelRuntimeContext context, AiModelCapability capability);

	default ChatModel createChatModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		throw new UnsupportedOperationException("ChatModel creation is not supported by this handler");
	}

	default EmbeddingModel createEmbeddingModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		throw new UnsupportedOperationException("EmbeddingModel creation is not supported by this handler");
	}

}
