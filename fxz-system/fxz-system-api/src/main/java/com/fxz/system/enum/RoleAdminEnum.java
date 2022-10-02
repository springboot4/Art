package com.fxz.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author fxz
 */
@Getter
@AllArgsConstructor
public enum RoleAdminEnum {

	/**
	 * 超级管理员
	 */
	SUPER_ADMIN("super_admin", "超级管理员"),

	/**
	 * 租户管理员
	 */
	TENANT_ADMIN("tenant_admin", "租户管理员");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

	/**
	 * 角色是否为管理员
	 * @param code 角色编码
	 * @return true or false
	 */
	public static boolean isAdmin(String code) {
		return Arrays.stream(values()).anyMatch(v -> v.getType().equals(code));
	}

}
