package com.art.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 *
 * @author fxz
 * @date 2025-10-18
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

	/**
	 * 文本消息
	 */
	TEXT("text", "文本"),

	/**
	 * 图片消息（预留）
	 */
	IMAGE("image", "图片"),

	/**
	 * 文件消息（预留）
	 */
	FILE("file", "文件"),

	/**
	 * 音频消息（预留）
	 */
	AUDIO("audio", "音频"),

	/**
	 * 视频消息（预留）
	 */
	VIDEO("video", "视频");

	/**
	 * 类型码
	 */
	private final String code;

	/**
	 * 类型描述
	 */
	private final String desc;

	/**
	 * 根据code获取枚举
	 * @param code 类型码
	 * @return 枚举
	 */
	public static MessageTypeEnum fromCode(String code) {
		for (MessageTypeEnum type : values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown message type code: " + code);
	}

}
