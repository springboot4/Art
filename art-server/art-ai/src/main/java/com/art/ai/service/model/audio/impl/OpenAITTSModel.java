package com.art.ai.service.model.audio.impl;

import com.art.ai.dto.audio.VoiceInfo;
import com.art.ai.service.model.audio.TTSModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAI 文字转语音模型
 *
 * @author fxz
 * @since 2026/02/28
 */
@Slf4j
public class OpenAITTSModel implements TTSModel {

	/**
	 * OpenAI TTS 支持的声音列表
	 */
	private static final List<VoiceInfo> VOICES = List.of(
			VoiceInfo.builder().id("alloy").name("Alloy").language("en").gender("neutral").description("中性声音").build(),
			VoiceInfo.builder().id("echo").name("Echo").language("en").gender("male").description("男性声音").build(),
			VoiceInfo.builder().id("fable").name("Fable").language("en").gender("neutral").description("中性声音").build(),
			VoiceInfo.builder().id("onyx").name("Onyx").language("en").gender("male").description("男性声音").build(),
			VoiceInfo.builder().id("nova").name("Nova").language("en").gender("female").description("女性声音").build(),
			VoiceInfo.builder()
				.id("shimmer")
				.name("Shimmer")
				.language("en")
				.gender("female")
				.description("女性声音")
				.build());

	// ==================== 配置 ====================

	private final WebClient webClient;

	private final ObjectMapper objectMapper;

	private final String modelName;

	/**
	 * 配置的默认音色
	 */
	private final String defaultVoice;

	// ==================== 构造函数 ====================

	public OpenAITTSModel(String apiKey, String baseUrl, String modelName, String defaultVoice) {
		this.objectMapper = new ObjectMapper();
		this.modelName = modelName;
		this.defaultVoice = defaultVoice;
		this.webClient = WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeader("Authorization", "Bearer " + apiKey)
			.build();
		log.info("初始化 OpenAI TTS 模型, baseUrl={}, modelName={}, defaultVoice={}", baseUrl, this.modelName,
				this.defaultVoice);
	}

	// ==================== 接口实现 ====================

	@Override
	public byte[] synthesize(String text, String voice) {
		log.info("调用 TTS API 进行文字转语音, model={}, voice={}, textLength={}", modelName, voice, text.length());

		try {
			// 构建请求体
			String effectiveVoice = voice != null ? voice : defaultVoice;
			String requestBody = objectMapper
				.writeValueAsString(new TTSRequest(modelName, text, effectiveVoice, "mp3"));

			log.info("TTS 请求体: {}", requestBody);

			// 发送请求并获取音频数据
			byte[] audioData = webClient.post()
				.uri("/audio/speech")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class)
							.defaultIfEmpty("Unknown error")
							.flatMap(errorBody -> {
								log.error("TTS API 错误响应: status={}, body={}", response.statusCode(), errorBody);
								return reactor.core.publisher.Mono.error(new RuntimeException(
										"TTS API 错误: " + response.statusCode() + " - " + errorBody));
							}))
				.bodyToMono(byte[].class)
				.block();

			return audioData != null ? audioData : new byte[0];

		}
		catch (Exception e) {
			log.error("TTS API 调用失败", e);
			throw new RuntimeException("文字转语音失败: " + e.getMessage(), e);
		}
	}

	@Override
	public Flux<byte[]> synthesizeStream(String text, String voice) {
		log.info("调用 TTS API (流式) 进行文字转语音, model={}, voice={}, textLength={}", modelName, voice, text.length());

		try {
			// 构建请求体
			String effectiveVoice = voice != null ? voice : defaultVoice;
			String requestBody = objectMapper
				.writeValueAsString(new TTSRequest(modelName, text, effectiveVoice, "mp3"));

			// 发送请求并获取流式音频数据
			return webClient.post()
				.uri("/audio/speech")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class)
							.defaultIfEmpty("Unknown error")
							.flatMap(errorBody -> {
								log.error("TTS API 流式错误响应: status={}, body={}", response.statusCode(), errorBody);
								return reactor.core.publisher.Mono.error(new RuntimeException(
										"TTS API 错误: " + response.statusCode() + " - " + errorBody));
							}))
				.bodyToFlux(byte[].class);

		}
		catch (Exception e) {
			log.error("TTS API 流式调用失败", e);
			return Flux.error(new RuntimeException("文字转语音失败: " + e.getMessage(), e));
		}
	}

	@Override
	public List<VoiceInfo> getVoices(String language) {
		if (language == null || language.isEmpty()) {
			return new ArrayList<>(VOICES);
		}
		return VOICES.stream().filter(v -> language.equals(v.getLanguage())).toList();
	}

	@Override
	public String getDefaultVoice() {
		return defaultVoice;
	}

	/**
	 * TTS 请求体
	 */
	private record TTSRequest(String model, String input, String voice, String response_format) {
	}

}
