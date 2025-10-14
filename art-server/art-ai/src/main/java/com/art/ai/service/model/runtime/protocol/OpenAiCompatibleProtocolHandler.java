package com.art.ai.service.model.runtime.protocol;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeConfig;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import com.art.core.common.exception.ArtException;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

/**
 * 适用于 OpenAI 兼容供应商
 *
 * @author fxz
 */
@Component
public class OpenAiCompatibleProtocolHandler implements ModelProtocolHandler, StreamingModelProtocolHandler {

	@Override
	public boolean supports(AiModelRuntimeContext context, AiModelCapability capability) {
		return context.getProtocolType().isOpenAiCompatible()
				&& (capability == AiModelCapability.CHAT || capability == AiModelCapability.EMBEDDING);
	}

	@Override
	public ChatModel createChatModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		ensureCapability(context, AiModelCapability.CHAT);

		AiModelRuntimeConfig config = context.getRuntimeConfig();
		AiModelInvokeOptions invokeOptions = options == null ? AiModelInvokeOptions.empty() : options;

		OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
			.apiKey(context.getPlatform().getApiKey())
			.modelName(config.getModelIdentifier());
		if (StringUtils.hasText(context.getPlatform().resolvedBaseUrl())) {
			builder.baseUrl(context.getPlatform().resolvedBaseUrl());
		}

		builder.maxRetries(intFrom(config.getParameter("maxRetries")).orElse(1));
		builder.logRequests(booleanFrom(config.getParameter("logRequests")).orElse(Boolean.TRUE));
		builder.logResponses(booleanFrom(config.getParameter("logResponses")).orElse(Boolean.TRUE));

		applyTemperature(builder, config, invokeOptions);
		applyTopP(builder, config, invokeOptions);
		applyMaxTokens(builder, config, invokeOptions);
		applyTimeout(builder, config, invokeOptions);

		return builder.build();
	}

	@Override
	public StreamingChatModel createStreamingChatModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		ensureCapability(context, AiModelCapability.CHAT);

		AiModelRuntimeConfig config = context.getRuntimeConfig();
		AiModelInvokeOptions invokeOptions = options == null ? AiModelInvokeOptions.empty() : options;

		OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder()
			.apiKey(context.getPlatform().getApiKey())
			.modelName(config.getModelIdentifier());

		if (StringUtils.hasText(context.getPlatform().resolvedBaseUrl())) {
			builder.baseUrl(context.getPlatform().resolvedBaseUrl());
		}

		builder.logRequests(booleanFrom(config.getParameter("logRequests")).orElse(Boolean.TRUE));
		builder.logResponses(booleanFrom(config.getParameter("logResponses")).orElse(Boolean.TRUE));

		applyTemperature(builder, config, invokeOptions);
		applyTopP(builder, config, invokeOptions);
		applyMaxTokens(builder, config, invokeOptions);
		applyTimeout(builder, config, invokeOptions);

		return builder.build();
	}

	@Override
	public EmbeddingModel createEmbeddingModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		ensureCapability(context, AiModelCapability.EMBEDDING);
		AiModelRuntimeConfig config = context.getRuntimeConfig();
		AiModelInvokeOptions invokeOptions = options == null ? AiModelInvokeOptions.empty() : options;

		OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder builder = OpenAiEmbeddingModel.builder()
			.apiKey(context.getPlatform().getApiKey())
			.modelName(config.getModelIdentifier());

		if (StringUtils.hasText(context.getPlatform().resolvedBaseUrl())) {
			builder.baseUrl(context.getPlatform().resolvedBaseUrl());
		}

		applyMaxRetries(builder, config);
		applyTimeout(builder, config, invokeOptions);
		applyDimensions(builder, config);

		builder.logRequests(true).logResponses(true);
		return builder.build();
	}

	private void applyMaxRetries(OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder builder,
			AiModelRuntimeConfig config) {
		builder.maxRetries(intFrom(config.getParameter("maxRetries")).orElse(1));
	}

	private void applyDimensions(OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder builder,
			AiModelRuntimeConfig config) {
		Integer dimensions = intFrom(config.getParameter("dimensions")).orElse(1024);
		builder.dimensions(dimensions);
	}

	private void ensureCapability(AiModelRuntimeContext context, AiModelCapability expected) {
		if (context.getCapability() != expected) {
			throw new ArtException(String.format("Model capability mismatch, expect=%s, actual=%s", expected,
					context.getCapability()));
		}
	}

	private void applyTemperature(OpenAiChatModel.OpenAiChatModelBuilder builder, AiModelRuntimeConfig config,
			AiModelInvokeOptions options) {
		BigDecimal temperature = options.getTemperature();
		if (temperature == null) {
			temperature = decimalFrom(config.getParameter("temperature")).orElse(null);
		}
		if (temperature != null) {
			builder.temperature(temperature.doubleValue());
		}
	}

	private void applyTemperature(OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder,
			AiModelRuntimeConfig config, AiModelInvokeOptions options) {
		BigDecimal temperature = options.getTemperature();
		if (temperature == null) {
			temperature = decimalFrom(config.getParameter("temperature")).orElse(null);
		}
		if (temperature != null) {
			builder.temperature(temperature.doubleValue());
		}
	}

	private void applyTopP(OpenAiChatModel.OpenAiChatModelBuilder builder, AiModelRuntimeConfig config,
			AiModelInvokeOptions options) {
		BigDecimal topP = options.getTopP();
		if (topP == null) {
			topP = decimalFrom(config.getParameter("topP")).orElse(null);
		}
		if (topP != null) {
			builder.topP(topP.doubleValue());
		}
	}

	private void applyTopP(OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder,
			AiModelRuntimeConfig config, AiModelInvokeOptions options) {
		BigDecimal topP = options.getTopP();
		if (topP == null) {
			topP = decimalFrom(config.getParameter("topP")).orElse(null);
		}
		if (topP != null) {
			builder.topP(topP.doubleValue());
		}
	}

	private void applyMaxTokens(OpenAiChatModel.OpenAiChatModelBuilder builder, AiModelRuntimeConfig config,
			AiModelInvokeOptions options) {
		Integer maxTokens = options.getMaxOutputTokens();
		if (maxTokens == null) {
			maxTokens = intFrom(config.getParameter("maxTokens"))
				.orElseGet(() -> intFromValue(config.getMaxOutputTokens()));
		}
		if (maxTokens != null) {
			builder.maxTokens(maxTokens);
		}
	}

	private void applyMaxTokens(OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder,
			AiModelRuntimeConfig config, AiModelInvokeOptions options) {
		Integer maxTokens = options.getMaxOutputTokens();
		if (maxTokens == null) {
			maxTokens = intFrom(config.getParameter("maxTokens"))
				.orElseGet(() -> intFromValue(config.getMaxOutputTokens()));
		}
		if (maxTokens != null) {
			builder.maxTokens(maxTokens);
		}
	}

	private void applyTimeout(OpenAiChatModel.OpenAiChatModelBuilder builder, AiModelRuntimeConfig config,
			AiModelInvokeOptions options) {
		Duration timeout = options.getTimeout();
		if (timeout == null) {
			timeout = durationFrom(config.getParameter("timeoutSeconds")).orElse(Duration.ofSeconds(60));
		}
		builder.timeout(timeout);
	}

	private void applyTimeout(OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder,
			AiModelRuntimeConfig config, AiModelInvokeOptions options) {
		Duration timeout = options.getTimeout();
		if (timeout == null) {
			timeout = durationFrom(config.getParameter("timeoutSeconds")).orElse(Duration.ofSeconds(60));
		}
		builder.timeout(timeout);
	}

	private void applyTimeout(OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder builder, AiModelRuntimeConfig config,
			AiModelInvokeOptions options) {
		Duration timeout = options.getTimeout();
		if (timeout == null) {
			timeout = durationFrom(config.getParameter("timeoutSeconds")).orElse(Duration.ofSeconds(30));
		}
		builder.timeout(timeout);
	}

	private Optional<Integer> intFrom(Optional<Object> value) {
		return value.map(this::intFromValue);
	}

	private Integer intFromValue(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number number) {
			return number.intValue();
		}
		if (value instanceof String text && StringUtils.hasText(text)) {
			try {
				return Integer.parseInt(text);
			}
			catch (NumberFormatException ignore) {
				return null;
			}
		}
		return null;
	}

	private Optional<BigDecimal> decimalFrom(Optional<Object> value) {
		return value.map(this::decimalFromValue);
	}

	private BigDecimal decimalFromValue(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof BigDecimal bigDecimal) {
			return bigDecimal;
		}
		if (value instanceof Number number) {
			return BigDecimal.valueOf(number.doubleValue());
		}
		if (value instanceof String text && StringUtils.hasText(text)) {
			try {
				return new BigDecimal(text);
			}
			catch (NumberFormatException ignore) {
				return null;
			}
		}
		return null;
	}

	private Optional<Boolean> booleanFrom(Optional<Object> value) {
		return value.map(this::booleanFromValue);
	}

	private Boolean booleanFromValue(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean bool) {
			return bool;
		}
		if (value instanceof Number number) {
			return number.intValue() != 0;
		}
		if (value instanceof String text) {
			return Boolean.parseBoolean(text);
		}
		return null;
	}

	private Optional<Duration> durationFrom(Optional<Object> value) {
		return value.map(this::durationFromValue);
	}

	private Duration durationFromValue(Object value) {
		Integer seconds = intFromValue(value);
		return seconds == null ? null : Duration.ofSeconds(seconds);
	}

}
