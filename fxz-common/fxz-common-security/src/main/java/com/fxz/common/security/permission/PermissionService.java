package com.fxz.common.security.permission;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

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
		// 如果为空，说明已经有权限
		if (ArrayUtil.isEmpty(permissions)) {
			return true;
		}

		// 获得当前登录的角色。如果为空，说明没有权限
		FxzAuthUser user = SecurityUtil.getUser();
		if (ObjectUtil.isEmpty(user)) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
	}

	/**
	 * 判断是否有角色
	 * <p>
	 * @param role 角色
	 * @return 是否
	 */
	public boolean hasRole(String role) {
		// todo
		return true;
		// return hasAnyRoles(role);
	}

	/**
	 * 判断是否有角色，任一一个即可
	 * @param roles 角色数组
	 * @return 是否
	 */
	public boolean hasAnyRoles(String... roles) {
		// todo
		return true;
		/*
		 * // 如果为空，说明已经有权限 if (ArrayUtil.isEmpty(roles)) { return true; }
		 *
		 * FxzAuthUser user = SecurityUtil.getUser(); if (ObjectUtil.isEmpty(user)) {
		 * return false; }
		 *
		 * String roleName = user.getRoleName(); if (StringUtils.isEmpty(roleName)) {
		 * return false; }
		 *
		 * String[] roleList = roleName.split(StringPool.COMMA); return
		 * CollUtil.containsAny(Arrays.asList(roleList), Sets.newHashSet(roles));
		 */
	}

}
