package com.art.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息状态枚举
 *
 * @author fxz
 * @date 2025-10-18
 */
@Getter
@AllArgsConstructor
public enum MessageStatusEnum {

	/**
	 * 已完成
	 */
	COMPLETED("completed", "已完成");

	/**
	 * 状态码
	 */
	private final String code;

	/**
	 * 状态描述
	 */
	private final String desc;

	/**
	 * 根据code获取枚举
	 * @param code 状态码
	 * @return 枚举
	 */
	public static MessageStatusEnum fromCode(String code) {
		for (MessageStatusEnum status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Unknown message status code: " + code);
	}

}
