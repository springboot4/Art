package com.art.ai.service.model.audio.impl;

import com.art.ai.dto.audio.AudioToTextResponse;
import com.art.ai.service.model.audio.Speech2TextModel;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.model.audio.AudioTranscriptionModel;
import dev.langchain4j.model.audio.AudioTranscriptionRequest;
import dev.langchain4j.model.audio.AudioTranscriptionResponse;
import dev.langchain4j.model.openai.OpenAiAudioTranscriptionModel;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Base64;

/**
 * OpenAI Whisper 语音转文字模型 (基于 langchain4j 实现)
 *
 * @author fxz
 * @since 2026/02/28
 */
@Slf4j
public class OpenAIWhisperModel implements Speech2TextModel {

	private static final String DEFAULT_BASE_URL = "https://api.openai.com/v1";

	private final AudioTranscriptionModel langchain4jModel;

	public OpenAIWhisperModel(String apiKey, String baseUrl, String modelName) {
		log.info("初始化 OpenAI Whisper 模型 (langchain4j), baseUrl={}", baseUrl);

		String normalizedBaseUrl = normalizeBaseUrl(baseUrl);

		this.langchain4jModel = OpenAiAudioTranscriptionModel.builder()
			.apiKey(apiKey)
			.baseUrl(normalizedBaseUrl)
			.modelName(modelName)
			.build();
	}

	@Override
	public AudioToTextResponse transcribe(InputStream audioData, String format, String language) {
		log.info("调用 langchain4j Whisper API 进行语音转文字, format={}, language={}", format, language);

		try {
			byte[] audioBytes = audioData.readAllBytes();

			// 根据 format 确定 MIME 类型
			String mimeType = getMimeType(format);
			String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
			Audio audio = Audio.builder().base64Data(base64Audio).mimeType(mimeType).build();

			AudioTranscriptionRequest request = AudioTranscriptionRequest.builder()
				.audio(audio)
				.language(language != null && !"auto".equals(language) ? language : null)
				.build();

			AudioTranscriptionResponse response = langchain4jModel.transcribe(request);

			return AudioToTextResponse.builder().text(response.text()).language(language).build();

		}
		catch (Exception e) {
			log.error("langchain4j Whisper API 调用失败", e);
			throw new RuntimeException("语音转文字失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 根据格式获取 MIME 类型
	 */
	private String getMimeType(String format) {
		if (format == null || format.isEmpty()) {
			return "audio/wav"; // 默认 wav
		}
		return switch (format.toLowerCase()) {
			case "mp3", "mpga", "mpeg" -> "audio/mpeg";
			case "wav" -> "audio/wav";
			case "m4a" -> "audio/m4a";
			case "webm" -> "audio/webm";
			case "ogg" -> "audio/ogg";
			case "flac" -> "audio/flac";
			case "opus" -> "audio/opus";
			case "mp4" -> "audio/mp4";
			default -> "audio/" + format.toLowerCase();
		};
	}

	/**
	 * 规范化 Base URL
	 */
	private String normalizeBaseUrl(String baseUrl) {
		if (baseUrl == null || baseUrl.isEmpty()) {
			return DEFAULT_BASE_URL;
		}

		// 移除末尾的斜杠
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}

		// 移除 /v1 后缀（如果存在），因为 OpenAiClient 会自动添加
		if (baseUrl.endsWith("/v1")) {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 3);
		}

		return baseUrl + "/v1";
	}

}
