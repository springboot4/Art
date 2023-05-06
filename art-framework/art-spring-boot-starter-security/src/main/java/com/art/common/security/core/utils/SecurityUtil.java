/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.security.core.utils;

import com.art.common.core.exception.FxzException;
import com.art.common.core.util.WebUtil;
import com.art.common.security.core.model.ArtAuthUser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;

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
	 * 获取当前用户权限
	 * @return 用户权限
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Authentication authentication = getAuthentication();
		return authentication.getAuthorities();
	}

	/**
	 * 获取用户
	 */
	public ArtAuthUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();

		if (principal instanceof ArtAuthUser) {
			return (ArtAuthUser) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public ArtAuthUser getUser() {
		Authentication authentication = getAuthentication();
		ArtAuthUser userDetail = getUser(authentication);
		if (userDetail == null) {
			throw new FxzException("获取用户信息失败");
		}
		return userDetail;
	}

	/**
	 * 获取客户端ID
	 * <p>
	 * 兼容两种方式获取OAuth2客户端信息（client_id、client_secret） <br/>
	 * 方式一：client_id、client_secret放在请求路径中 <br/>
	 * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA==
	 * 明文等于 client:secret
	 */
	@SneakyThrows
	public String getClientId() {
		HttpServletRequest request = WebUtil.getRequest();
		BasicAuthenticationConverter converter = new BasicAuthenticationConverter();

		return converter.convert(request).getName();
	}

	public String getGrantType() {
		HttpServletRequest request = WebUtil.getRequest();

		// 从请求路径中获取
		return request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
	}

	/**
	 * 获取请求中携带的租户id
	 * @return 租户id
	 */
	public static Long getTenantId(HttpServletRequest request) {
		String header = request.getHeader("TENANT-ID");
		String parameter = request.getParameter("TENANT-ID");

		if (Objects.isNull(header) && Objects.isNull(parameter)) {
			return null;
		}
		return Objects.nonNull(header) ? Long.valueOf(header) : Long.valueOf(parameter);
	}

}