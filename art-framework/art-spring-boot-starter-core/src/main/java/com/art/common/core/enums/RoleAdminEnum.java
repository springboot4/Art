/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.core.enums;

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
