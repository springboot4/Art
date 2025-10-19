package com.art.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话状态枚举
 *
 * @author fxz
 * @date 2025-10-18
 */
@Getter
@AllArgsConstructor
public enum ConversationStatusEnum {

	/**
	 * 活跃中
	 */
	ACTIVE("active", "活跃中"),

	/**
	 * 已归档
	 */
	ARCHIVED("archived", "已归档"),

	/**
	 * 已删除
	 */
	DELETED("deleted", "已删除");

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
	public static ConversationStatusEnum fromCode(String code) {
		for (ConversationStatusEnum status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Unknown conversation status code: " + code);
	}

}
