package com.art.ai.service.model.audio;

import com.art.ai.dto.audio.AudioToTextResponse;

import java.io.InputStream;

/**
 * 语音转文字模型接口
 *
 * @author fxz
 * @since 2026/02/28
 */
public interface Speech2TextModel {

	/**
	 * 转录音频为文本
	 * @param audioData 音频数据流
	 * @param format 音频格式 (mp3, wav, m4a, webm 等)
	 * @param language 语言代码 (如 zh, en, auto)
	 * @return 转录结果
	 */
	AudioToTextResponse transcribe(InputStream audioData, String format, String language);

}
