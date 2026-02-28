package com.art.ai.dto.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 语音转文字响应
 *
 * @author fxz
 * @since 2026/02/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioToTextResponse {

	/**
	 * 识别的文本内容
	 */
	private String text;

	/**
	 * 识别的语言
	 */
	private String language;

	/**
	 * 识别耗时 (毫秒)
	 */
	private Long duration;

}
