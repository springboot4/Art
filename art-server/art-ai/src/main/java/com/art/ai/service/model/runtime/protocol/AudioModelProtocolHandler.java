package com.art.ai.service.model.runtime.protocol;

import com.art.ai.service.model.audio.Speech2TextModel;
import com.art.ai.service.model.audio.TTSModel;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;

/**
 * 音频模型协议处理器接口
 *
 * @author fxz
 * @since 2026/02/28
 */
public interface AudioModelProtocolHandler {

	/**
	 * 是否支持语音转文字
	 * @param context 模型运行时上下文
	 * @return 是否支持
	 */
	boolean supportsSpeech2Text(AiModelRuntimeContext context);

	/**
	 * 是否支持文字转语音
	 * @param context 模型运行时上下文
	 * @return 是否支持
	 */
	boolean supportsTTS(AiModelRuntimeContext context);

	/**
	 * 创建语音转文字模型
	 * @param context 模型运行时上下文
	 * @param options 调用选项
	 * @return STT 模型实例
	 */
	Speech2TextModel createSpeech2TextModel(AiModelRuntimeContext context, AiModelInvokeOptions options);

	/**
	 * 创建文字转语音模型
	 * @param context 模型运行时上下文
	 * @param options 调用选项
	 * @return TTS 模型实例
	 */
	TTSModel createTTSModel(AiModelRuntimeContext context, AiModelInvokeOptions options);

}
