package com.art.ai.core.constants;

/**
 * 模型协议枚举
 *
 * @author fxz
 */
public enum ModelProtocolType {

	/**
	 * OpenAI 兼容协议
	 */
	OPENAI_COMPATIBLE,

	/**
	 * 厂商原生协议
	 */
	VENDOR_NATIVE;

	public boolean isOpenAiCompatible() {
		return this == OPENAI_COMPATIBLE;
	}

}
