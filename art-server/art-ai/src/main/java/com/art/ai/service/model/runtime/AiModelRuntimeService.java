package com.art.ai.service.model.runtime;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.core.constants.ModelProtocolType;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import com.art.ai.dao.dataobject.AiModelDO;
import com.art.ai.dao.dataobject.AiModelPlatformDO;
import com.art.ai.manager.AiModelManager;
import com.art.ai.manager.AiModelPlatformManager;
import com.art.ai.service.model.runtime.protocol.ModelProtocolHandler;
import com.art.ai.service.model.runtime.protocol.ModelProtocolRegistry;
import com.art.core.common.exception.ArtException;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author fxz
 */
@Service
@RequiredArgsConstructor
public class AiModelRuntimeService {

	private final AiModelManager aiModelManager;

	private final AiModelPlatformManager aiModelPlatformManager;

	private final ModelProtocolRegistry modelProtocolRegistry;

	public AiModelRuntimeContext resolveContext(Long platformId, Long modelId) {
		Assert.notNull(modelId, "modelId must not be null");

		AiModelDO model = Optional.ofNullable(aiModelManager.findById(modelId))
			.orElseThrow(() -> new ArtException(String.format("模型不存在: %s", modelId)));

		if (!model.isEnabled()) {
			throw new ArtException(String.format("模型未启用: %s", modelId));
		}

		Long resolvedPlatformId = Optional.ofNullable(platformId).orElse(model.getPlatform());
		Assert.notNull(resolvedPlatformId, "platformId must not be null after resolution");

		if (model.getPlatform() != null && !model.getPlatform().equals(resolvedPlatformId)) {
			throw new ArtException(String.format("模型 %s 不属于平台 %s", modelId, resolvedPlatformId));
		}

		AiModelPlatformDO platform = Optional.ofNullable(aiModelPlatformManager.findById(resolvedPlatformId))
			.orElseThrow(() -> new ArtException(String.format("模型平台不存在: %s", resolvedPlatformId)));

		if (platform.protocolType() == ModelProtocolType.OPENAI_COMPATIBLE
				&& !StringUtils.hasText(platform.getApiKey())) {
			throw new ArtException(String.format("平台 %s 缺少 API Key", resolvedPlatformId));
		}

		return AiModelRuntimeContext.builder()
			.model(model)
			.platform(platform)
			.runtimeConfig(model.runtimeConfig())
			.capability(model.capability())
			.protocolType(platform.protocolType())
			.build();
	}

	public ChatModel acquireChatModel(Long platformId, Long modelId, AiModelInvokeOptions options) {
		AiModelRuntimeContext context = resolveContext(platformId, modelId);
		return acquireChatModel(context, options);
	}

	public ChatModel acquireChatModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		if (context == null) {
			throw new ArtException("模型上下文不能为空");
		}
		if (context.getCapability() != AiModelCapability.CHAT) {
			throw new ArtException(
					String.format("模型 %s 类型 %s 不支持 Chat 能力", context.getModel().getId(), context.getCapability()));
		}
		ModelProtocolHandler handler = modelProtocolRegistry.resolve(context, AiModelCapability.CHAT);
		AiModelInvokeOptions effectiveOptions = options == null ? AiModelInvokeOptions.empty() : options;
		return handler.createChatModel(context, effectiveOptions);
	}

	public ChatModel acquireChatModel(Long platformId, Long modelId) {
		return acquireChatModel(platformId, modelId, AiModelInvokeOptions.empty());
	}

	public EmbeddingModel acquireEmbeddingModel(Long platformId, Long modelId, AiModelInvokeOptions options) {
		AiModelRuntimeContext context = resolveContext(platformId, modelId);
		if (context.getCapability() != AiModelCapability.EMBEDDING) {
			throw new ArtException(String.format("模型 %s 类型 %s 不支持 Embedding 能力", modelId, context.getCapability()));
		}
		ModelProtocolHandler handler = modelProtocolRegistry.resolve(context, AiModelCapability.EMBEDDING);
		AiModelInvokeOptions effectiveOptions = options == null ? AiModelInvokeOptions.empty() : options;
		return handler.createEmbeddingModel(context, effectiveOptions);
	}

	public EmbeddingModel acquireEmbeddingModel(Long platformId, Long modelId) {
		return acquireEmbeddingModel(platformId, modelId, AiModelInvokeOptions.empty());
	}

}
