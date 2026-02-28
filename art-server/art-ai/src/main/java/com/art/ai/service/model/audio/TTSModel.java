package com.art.ai.service.model.audio;

import com.art.ai.dto.audio.VoiceInfo;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 文字转语音模型接口
 *
 * @author fxz
 * @since 2026/02/28
 */
public interface TTSModel {

	/**
	 * 同步合成语音
	 * @param text 要合成的文本
	 * @param voice 声音 ID
	 * @return 音频数据 (MP3 格式)
	 */
	byte[] synthesize(String text, String voice);

	/**
	 * 流式合成语音
	 * @param text 要合成的文本
	 * @param voice 声音 ID
	 * @return 音频数据流 (MP3 格式)
	 */
	Flux<byte[]> synthesizeStream(String text, String voice);

	/**
	 * 获取可用声音列表
	 * @param language 语言代码 (可选，用于过滤)
	 * @return 声音列表
	 */
	List<VoiceInfo> getVoices(String language);

	/**
	 * 获取默认声音
	 * @return 默认声音 ID
	 */
	default String getDefaultVoice() {
		return "alloy";
	}

}
