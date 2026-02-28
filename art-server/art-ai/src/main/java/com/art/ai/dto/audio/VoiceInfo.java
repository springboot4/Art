package com.art.ai.dto.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 声音信息
 *
 * @author fxz
 * @since 2026/02/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceInfo {

	/**
	 * 声音 ID
	 */
	private String id;

	/**
	 * 声音名称
	 */
	private String name;

	/**
	 * 语言代码 (如 zh, en)
	 */
	private String language;

	/**
	 * 性别 (male, female, neutral)
	 */
	private String gender;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 示例音频 URL
	 */
	private String sampleUrl;

}
