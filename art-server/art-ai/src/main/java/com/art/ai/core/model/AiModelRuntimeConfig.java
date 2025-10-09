package com.art.ai.core.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * 从持久化的 {@code AiModelDO} 记录中提取的规范化配置。
 *
 * @author fxz
 */
public class AiModelRuntimeConfig implements Serializable {

	@Serial
	private static final long serialVersionUID = -4431431265865798644L;

	private final Long contextWindowTokens;

	private final Long maxInputTokens;

	private final Long maxOutputTokens;

	private final String modelIdentifier;

	private final Map<String, Object> parameters;

	private AiModelRuntimeConfig(Builder builder) {
		this.contextWindowTokens = builder.contextWindowTokens;
		this.maxInputTokens = builder.maxInputTokens;
		this.maxOutputTokens = builder.maxOutputTokens;
		this.modelIdentifier = builder.modelIdentifier;
		this.parameters = builder.parameters == null ? Collections.emptyMap()
				: Collections.unmodifiableMap(builder.parameters);
	}

	public Long getContextWindowTokens() {
		return contextWindowTokens;
	}

	public Long getMaxInputTokens() {
		return maxInputTokens;
	}

	public Long getMaxOutputTokens() {
		return maxOutputTokens;
	}

	public String getModelIdentifier() {
		return modelIdentifier;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public Optional<Object> getParameter(String key) {
		return Optional.ofNullable(parameters.get(key));
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private Long contextWindowTokens;

		private Long maxInputTokens;

		private Long maxOutputTokens;

		private String modelIdentifier;

		private Map<String, Object> parameters;

		public Builder contextWindowTokens(Long value) {
			this.contextWindowTokens = value;
			return this;
		}

		public Builder maxInputTokens(Long value) {
			this.maxInputTokens = value;
			return this;
		}

		public Builder maxOutputTokens(Long value) {
			this.maxOutputTokens = value;
			return this;
		}

		public Builder modelIdentifier(String value) {
			this.modelIdentifier = value;
			return this;
		}

		public Builder parameters(Map<String, Object> value) {
			this.parameters = value;
			return this;
		}

		public AiModelRuntimeConfig build() {
			return new AiModelRuntimeConfig(this);
		}

	}

}
