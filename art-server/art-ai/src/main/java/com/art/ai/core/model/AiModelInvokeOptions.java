package com.art.ai.core.model;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * @author fxz
 */
@Getter
public class AiModelInvokeOptions implements Serializable {

	@Serial
	private static final long serialVersionUID = 6770683672069595970L;

	private static final AiModelInvokeOptions EMPTY = new Builder().build();

	private final BigDecimal temperature;

	private final BigDecimal topP;

	private final Integer maxOutputTokens;

	private final Duration timeout;

	private final Map<String, Object> extraParameters;

	private AiModelInvokeOptions(Builder builder) {
		this.temperature = builder.temperature;
		this.topP = builder.topP;
		this.maxOutputTokens = builder.maxOutputTokens;
		this.timeout = builder.timeout;
		this.extraParameters = builder.extraParameters == null ? Collections.emptyMap()
				: Map.copyOf(builder.extraParameters);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static AiModelInvokeOptions empty() {
		return EMPTY;
	}

	public static final class Builder {

		private BigDecimal temperature;

		private BigDecimal topP;

		private Integer maxOutputTokens;

		private Duration timeout;

		private Map<String, Object> extraParameters;

		public Builder temperature(BigDecimal value) {
			this.temperature = value;
			return this;
		}

		public Builder topP(BigDecimal value) {
			this.topP = value;
			return this;
		}

		public Builder maxOutputTokens(Integer value) {
			this.maxOutputTokens = value;
			return this;
		}

		public Builder timeout(Duration value) {
			this.timeout = value;
			return this;
		}

		public Builder extraParameters(Map<String, Object> value) {
			this.extraParameters = value;
			return this;
		}

		public AiModelInvokeOptions build() {
			return new AiModelInvokeOptions(this);
		}

	}

}
