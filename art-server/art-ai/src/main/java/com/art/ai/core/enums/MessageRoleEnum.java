package com.art.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息角色枚举
 *
 * @author fxz
 * @date 2025-10-18
 */
@Getter
@AllArgsConstructor
public enum MessageRoleEnum {

	/**
	 * 用户
	 */
	USER("user", "用户"),

	/**
	 * 助手
	 */
	ASSISTANT("assistant", "助手"),

	/**
	 * 系统
	 */
	SYSTEM("system", "系统");

	/**
	 * 角色码
	 */
	private final String code;

	/**
	 * 角色描述
	 */
	private final String desc;

	/**
	 * 根据code获取枚举
	 * @param code 角色码
	 * @return 枚举
	 */
	public static MessageRoleEnum fromCode(String code) {
		for (MessageRoleEnum role : values()) {
			if (role.getCode().equals(code)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Unknown message role code: " + code);
	}

}
