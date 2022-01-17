package com.fxz.common.security.util;

import com.fxz.common.core.exception.FxzException;
import com.fxz.common.security.entity.FxzAuthUser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring security 工具类
 *
 * @author fxz
 */
@UtilityClass
public class SecurityUtil {

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public FxzAuthUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof FxzAuthUser) {
			return (FxzAuthUser) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	@SneakyThrows
	public FxzAuthUser getUser() {
		Authentication authentication = getAuthentication();
		FxzAuthUser userDetail = getUser(authentication);
		if (userDetail == null) {
			throw new FxzException("获取用户信息失败");
		}
		return userDetail;
	}

}