package com.art.ai.service.model.runtime;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.service.model.runtime.protocol.StreamingModelProtocolHandler;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import com.art.core.common.exception.ArtException;
import dev.langchain4j.model.chat.StreamingChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流式模型运行时服务。
 *
 * @author fxz
 */
@Service
@RequiredArgsConstructor
public class AiStreamingModelRuntimeService {

	private final AiModelRuntimeService aiModelRuntimeService;

	private final List<StreamingModelProtocolHandler> streamingHandlers;

	public StreamingChatModel acquireStreamingChatModel(Long platformId, Long modelId, AiModelInvokeOptions options) {
		AiModelRuntimeContext context = aiModelRuntimeService.resolveContext(platformId, modelId);
		if (context.getCapability() != AiModelCapability.CHAT) {
			throw new ArtException(String.format("模型 %s 类型 %s 不支持流式 Chat 能力", modelId, context.getCapability()));
		}

		StreamingModelProtocolHandler handler = streamingHandlers.stream()
			.filter(h -> h.supports(context, AiModelCapability.CHAT))
			.findFirst()
			.orElseThrow(() -> new ArtException(String.format("未找到流式协议处理器, capability=%s, protocol=%s",
					AiModelCapability.CHAT, context.getProtocolType())));

		AiModelInvokeOptions effectiveOptions = options == null ? AiModelInvokeOptions.empty() : options;
		return handler.createStreamingChatModel(context, effectiveOptions);
	}

}
