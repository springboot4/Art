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

package com.art.common.security.permission;

import cn.hutool.core.util.ArrayUtil;
import com.art.common.security.entity.FxzAuthUser;
import com.art.common.security.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author fxz
 */
@Slf4j
public class PermissionService {

	/**
	 * 判断是否有权限
	 * @param permission 权限
	 * @return 是否
	 */
	public boolean hasPermission(String permission) {
		return hasAnyPermissions(permission);
	}

	/**
	 * 判断是否有权限，任一一个即可
	 * @param permissions 权限
	 * @return 是否
	 */
	public boolean hasAnyPermissions(String... permissions) {
		// 如果为空 说明有权限
		if (ArrayUtil.isEmpty(permissions)) {
			return true;
		}

		// 获得当前登录的角色 如果为空 说明没有权限
		FxzAuthUser user = SecurityUtil.getUser();
		if (Objects.isNull(user)) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = SecurityUtil.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
	}

}
