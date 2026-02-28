package com.art.ai.service.audio;

import com.art.ai.dto.audio.AudioToTextResponse;
import com.art.ai.dto.audio.VoiceInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 音频服务接口
 *
 * @author fxz
 * @since 2026/02/28
 */
public interface AudioService {

	/**
	 * 语音转文字
	 * @param appId 应用 ID
	 * @param audioData 音频数据
	 * @param format 音频格式 (mp3, wav, m4a 等)
	 * @param language 语言代码
	 * @return 转录结果
	 */
	AudioToTextResponse audioToText(Long appId, InputStream audioData, String format, String language);

	/**
	 * 文字转语音 (同步)
	 * @param appId 应用 ID
	 * @param text 要转换的文本
	 * @param voice 声音 ID
	 * @return 音频数据 (MP3 格式)
	 */
	byte[] textToAudio(Long appId, String text, String voice);

	/**
	 * 文字转语音 (流式)
	 * @param appId 应用 ID
	 * @param text 要转换的文本
	 * @param voice 声音 ID
	 * @param outputStream 输出流
	 */
	void textToAudioStream(Long appId, String text, String voice, OutputStream outputStream);

	/**
	 * 获取可用声音列表
	 * @param appId 应用 ID
	 * @param language 语言代码 (可选)
	 * @return 声音列表
	 */
	List<VoiceInfo> getVoices(Long appId, String language);

}
