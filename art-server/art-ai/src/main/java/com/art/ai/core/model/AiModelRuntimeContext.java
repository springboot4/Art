package com.art.ai.core.model;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.core.constants.ModelProtocolType;
import com.art.ai.dao.dataobject.AiModelDO;
import com.art.ai.dao.dataobject.AiModelPlatformDO;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 运行时上下文
 *
 * @author fxz
 */
@Getter
public class AiModelRuntimeContext implements Serializable {

	@Serial
	private static final long serialVersionUID = 2089106670613861513L;

	private final AiModelDO model;

	private final AiModelPlatformDO platform;

	private final AiModelRuntimeConfig runtimeConfig;

	private final AiModelCapability capability;

	private final ModelProtocolType protocolType;

	private AiModelRuntimeContext(Builder builder) {
		this.model = builder.model;
		this.platform = builder.platform;
		this.runtimeConfig = builder.runtimeConfig;
		this.capability = builder.capability;
		this.protocolType = builder.protocolType;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private AiModelDO model;

		private AiModelPlatformDO platform;

		private AiModelRuntimeConfig runtimeConfig;

		private AiModelCapability capability;

		private ModelProtocolType protocolType;

		public Builder model(AiModelDO value) {
			this.model = value;
			return this;
		}

		public Builder platform(AiModelPlatformDO value) {
			this.platform = value;
			return this;
		}

		public Builder runtimeConfig(AiModelRuntimeConfig value) {
			this.runtimeConfig = value;
			return this;
		}

		public Builder capability(AiModelCapability value) {
			this.capability = value;
			return this;
		}

		public Builder protocolType(ModelProtocolType value) {
			this.protocolType = value;
			return this;
		}

		public AiModelRuntimeContext build() {
			return new AiModelRuntimeContext(this);
		}

	}

}
