package com.art.ai.dto.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文字转语音请求
 *
 * @author fxz
 * @since 2026/02/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextToAudioRequest {

	/**
	 * 要转换的文本
	 */
	private String text;

	/**
	 * 声音 ID
	 */
	private String voice;

	/**
	 * 语言代码
	 */
	private String language;

	/**
	 * 语速 (0.5 - 2.0)
	 */
	private Double speed;

}
