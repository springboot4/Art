package com.art.ai.service.model.runtime.protocol;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.core.model.AiModelRuntimeContext;
import com.art.core.common.exception.ArtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模型协议注册器
 *
 * @author fxz
 */
@Component
@RequiredArgsConstructor
public class ModelProtocolRegistry {

	private final List<ModelProtocolHandler> handlers;

	public ModelProtocolHandler resolve(AiModelRuntimeContext context, AiModelCapability capability) {
		return handlers.stream()
			.filter(handler -> handler.supports(context, capability))
			.findFirst()
			.orElseThrow(() -> new ArtException(String.format("No protocol handler for capability=%s, protocol=%s",
					capability, context.getProtocolType())));
	}

}
