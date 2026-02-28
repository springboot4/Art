package com.art.ai.service.model.runtime.protocol.impl;

import com.art.ai.core.constants.ModelProtocolType;
import com.art.ai.dao.dataobject.AiModelPlatformDO;
import com.art.ai.service.model.audio.Speech2TextModel;
import com.art.ai.service.model.audio.TTSModel;
import com.art.ai.service.model.audio.impl.OpenAITTSModel;
import com.art.ai.service.model.audio.impl.OpenAIWhisperModel;
import com.art.ai.service.model.runtime.protocol.AudioModelProtocolHandler;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import org.springframework.stereotype.Component;

/**
 * OpenAI 音频模型协议处理器
 *
 * @author fxz
 * @since 2026/02/28
 */
@Component
public class OpenAIAudioModelProtocolHandler implements AudioModelProtocolHandler {

	@Override
	public boolean supportsSpeech2Text(AiModelRuntimeContext context) {
		return isOpenAICompatible(context);
	}

	@Override
	public boolean supportsTTS(AiModelRuntimeContext context) {
		return isOpenAICompatible(context);
	}

	@Override
	public Speech2TextModel createSpeech2TextModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		AiModelPlatformDO platform = context.getPlatform();
		String apiKey = platform.getApiKey();
		String baseUrl = extractBaseUrl(platform);

		return new OpenAIWhisperModel(apiKey, baseUrl, context.getModel().getName());
	}

	@Override
	public TTSModel createTTSModel(AiModelRuntimeContext context, AiModelInvokeOptions options) {
		AiModelPlatformDO platform = context.getPlatform();
		String apiKey = platform.getApiKey();
		String baseUrl = extractBaseUrl(platform);
		String defaultVoice = context.getModel().getDefaultVoice();

		return new OpenAITTSModel(apiKey, baseUrl, context.getModel().getName(), defaultVoice);
	}

	/**
	 * 判断是否为 OpenAI 兼容协议
	 */
	private boolean isOpenAICompatible(AiModelRuntimeContext context) {
		if (context.getProtocolType() == null) {
			return false;
		}
		return context.getProtocolType() == ModelProtocolType.OPENAI_COMPATIBLE;
	}

	/**
	 * 提取 Base URL
	 */
	private String extractBaseUrl(AiModelPlatformDO platform) {
		String baseUrl = platform.resolvedBaseUrl();
		if (baseUrl != null && !baseUrl.isEmpty()) {
			// 移除末尾的斜杠
			if (baseUrl.endsWith("/")) {
				baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
			}
			// 移除 /v1 后缀（如果存在）
			if (baseUrl.endsWith("/v1")) {
				baseUrl = baseUrl.substring(0, baseUrl.length() - 3);
			}
			return baseUrl + "/v1";
		}
		return "https://api.openai.com/v1";
	}

}
