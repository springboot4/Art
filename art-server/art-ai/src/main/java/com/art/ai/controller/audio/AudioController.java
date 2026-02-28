package com.art.ai.controller.audio;

import com.art.ai.dto.audio.AudioToTextResponse;
import com.art.ai.dto.audio.TextToAudioRequest;
import com.art.ai.dto.audio.VoiceInfo;
import com.art.ai.service.audio.AudioService;
import com.art.core.common.exception.ArtException;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 音频 API 控制器
 *
 * @author fxz
 * @since 2026/02/28
 */
@Slf4j
@Tag(name = "音频 API")
@RestController
@RequestMapping("/v1/audio")
@RequiredArgsConstructor
public class AudioController {

	// ==================== 常量定义 ====================

	/**
	 * 支持的音频格式
	 */
	private static final Set<String> SUPPORTED_FORMATS = new HashSet<>(
			Arrays.asList("mp3", "m4a", "wav", "webm", "amr", "mpga"));

	/**
	 * 默认语言
	 */
	private static final String DEFAULT_LANGUAGE = "auto";

	// ==================== 依赖注入 ====================

	private final AudioService audioService;

	// ==================== API 端点 ====================

	/**
	 * 语音转文字
	 * @param file 音频文件
	 * @param appId 应用 ID
	 * @param language 语言代码 (zh, en, auto)
	 * @return 转录结果
	 */
	@Operation(summary = "语音转文字")
	@PostMapping("/to-text")
	public Result<AudioToTextResponse> audioToText(@RequestPart("file") MultipartFile file,
			@RequestParam("appId") Long appId,
			@RequestParam(value = "language", required = false, defaultValue = DEFAULT_LANGUAGE) String language) {

		// 1. 校验文件
		validateAudioFile(file);

		// 2. 调用服务
		try {
			AudioToTextResponse response = audioService.audioToText(appId, file.getInputStream(),
					getFileExtension(file.getOriginalFilename()), language);
			return Result.success(response);
		}
		catch (IOException e) {
			log.error("读取音频文件失败", e);
			throw new ArtException("读取音频文件失败: " + e.getMessage());
		}
	}

	/**
	 * 文字转语音 (流式)
	 * @param appId 应用 ID
	 * @param request TTS 请求
	 * @return 音频流
	 */
	@Operation(summary = "文字转语音 (流式)")
	@PostMapping(value = "/to-audio", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public StreamingResponseBody textToAudio(@RequestParam("appId") Long appId,
			@RequestBody TextToAudioRequest request) {

		return outputStream -> audioService.textToAudioStream(appId, request.getText(), request.getVoice(),
				outputStream);
	}

	/**
	 * 获取可用声音列表
	 * @param appId 应用 ID
	 * @param language 语言代码 (可选)
	 * @return 声音列表
	 */
	@Operation(summary = "获取可用声音列表")
	@GetMapping("/voices")
	public Result<List<VoiceInfo>> getVoices(@RequestParam("appId") Long appId,
			@RequestParam(value = "language", required = false) String language) {

		List<VoiceInfo> voices = audioService.getVoices(appId, language);
		return Result.success(voices);
	}

	/**
	 * 校验音频文件
	 */
	private void validateAudioFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ArtException("音频文件不能为空");
		}

		// 优先从 Content-Type 判断格式
		String contentType = file.getContentType();
		if (contentType != null && contentType.startsWith("audio/")) {
			// audio/mpeg -> mp3, audio/wav -> wav, etc.
			String formatFromContentType = contentType.substring("audio/".length());
			// mpeg 格式对应 mp3
			if ("mpeg".equals(formatFromContentType)) {
				formatFromContentType = "mp3";
			}
			if (SUPPORTED_FORMATS.contains(formatFromContentType.toLowerCase())) {
				// 有效的音频格式，跳过文件名校验
				validateSize(file);
				return;
			}
		}

		// 从文件名判断
		validateByFilename(file);
		validateSize(file);
	}

	/**
	 * 校验文件大小
	 */
	private void validateSize(MultipartFile file) {
		if (file.getSize() > 30 * 1024 * 1024) {
			throw new ArtException("文件大小超过限制 (30MB)");
		}
	}

	/**
	 * 通过文件名校验
	 */
	private void validateByFilename(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (filename == null || !filename.contains(".")) {
			throw new ArtException("无法识别文件格式，请确保文件名包含扩展名");
		}

		String extension = getFileExtension(filename);
		if (!SUPPORTED_FORMATS.contains(extension.toLowerCase())) {
			throw new ArtException("不支持的音频格式: " + extension + "，支持的格式: " + SUPPORTED_FORMATS);
		}
	}

	/**
	 * 获取文件扩展名
	 */
	private String getFileExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
	}

}
