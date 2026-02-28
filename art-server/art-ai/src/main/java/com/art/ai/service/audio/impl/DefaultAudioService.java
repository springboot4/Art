package com.art.ai.service.audio.impl;

import com.art.ai.dto.audio.AudioToTextResponse;
import com.art.ai.dto.audio.VoiceInfo;
import com.art.ai.service.audio.AudioService;
import com.art.ai.service.model.audio.Speech2TextModel;
import com.art.ai.service.model.audio.TTSModel;
import com.art.ai.service.model.runtime.AudioModelRuntimeService;
import com.art.core.common.exception.ArtException;
import com.art.common.tenant.context.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 音频服务默认实现
 *
 * @author fxz
 * @since 2026/02/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAudioService implements AudioService {

	private static final String DEFAULT_LANGUAGE = "auto";

	private static final int MAX_TEXT_LENGTH = 4096;

	private final AudioModelRuntimeService audioModelRuntimeService;

	@Override
	public AudioToTextResponse audioToText(Long appId, InputStream audioData, String format, String language) {
		Long tenantId = TenantContextHolder.getTenantId();
		Speech2TextModel model = audioModelRuntimeService.acquireDefaultSpeech2TextModel(tenantId);

		long startTime = System.currentTimeMillis();
		try {
			AudioToTextResponse response = model.transcribe(audioData, format,
					language != null ? language : DEFAULT_LANGUAGE);
			response.setDuration(System.currentTimeMillis() - startTime);
			return response;
		}
		catch (Exception e) {
			log.error("语音转文字失败, appId={}", appId, e);
			throw new ArtException("语音转文字失败: " + e.getMessage());
		}
	}

	@Override
	public byte[] textToAudio(Long appId, String text, String voice) {
		validateText(text);

		Long tenantId = TenantContextHolder.getTenantId();
		TTSModel model = audioModelRuntimeService.acquireDefaultTTSModel(tenantId);

		try {
			String actualVoice = (voice != null && !voice.isEmpty()) ? voice : model.getDefaultVoice();
			return model.synthesize(text, actualVoice);
		}
		catch (Exception e) {
			log.error("文字转语音失败, appId={}", appId, e);
			throw new ArtException("文字转语音失败: " + e.getMessage());
		}
	}

	@Override
	public void textToAudioStream(Long appId, String text, String voice, OutputStream outputStream) {
		validateText(text);

		Long tenantId = TenantContextHolder.getTenantId();
		TTSModel model = audioModelRuntimeService.acquireDefaultTTSModel(tenantId);

		try {
			// 使用传入的音色，如果为空则使用模型配置的默认音色
			String actualVoice = (voice != null && !voice.isEmpty()) ? voice : model.getDefaultVoice();

			// 调用模型流式合成并写入输出流
			model.synthesizeStream(text, actualVoice).doOnNext(chunk -> {
				try {
					outputStream.write(chunk);
				}
				catch (Exception e) {
					throw new RuntimeException("写入音频流失败", e);
				}
			}).doOnError(e -> log.error("文字转语音流式失败, appId={}", appId, e)).then().block();
		}
		catch (Exception e) {
			log.error("文字转语音失败, appId={}", appId, e);
			throw new ArtException("文字转语音失败: " + e.getMessage());
		}
	}

	@Override
	public List<VoiceInfo> getVoices(Long appId, String language) {
		Long tenantId = TenantContextHolder.getTenantId();
		TTSModel model = audioModelRuntimeService.acquireDefaultTTSModel(tenantId);
		return model.getVoices(language);
	}

	/**
	 * 校验文本
	 */
	private void validateText(String text) {
		if (text == null || text.trim().isEmpty()) {
			throw new ArtException("文本不能为空");
		}
		if (text.length() > MAX_TEXT_LENGTH) {
			throw new ArtException("文本长度超过限制: " + MAX_TEXT_LENGTH);
		}
	}

}
