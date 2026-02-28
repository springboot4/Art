package com.art.ai.core.constants;

/**
 * 模型特性
 *
 * @author fxz
 */
public enum ModelFeature {

	/**
	 * 支持原生结构化输出
	 */
	STRUCTURED_OUTPUT_NATIVE,

	/**
	 * 支持 JSON Mode
	 */
	JSON_MODE,

	/**
	 * 支持 Function Call / 工具调用
	 */
	FUNCTION_CALL,

	/**
	 * 支持语音转文字 (Speech-to-Text / STT)
	 */
	SPEECH_TO_TEXT,

	/**
	 * 支持文字转语音 (Text-to-Speech / TTS)
	 */
	TEXT_TO_SPEECH

}
