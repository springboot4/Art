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

package com.art.common.security.service;

import com.art.common.core.exception.FxzException;
import com.art.common.security.util.SecurityUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;

/**
 * 认证前，根据客户端id选择UserDetailsService
 *
 * @author fxz
 */
@Slf4j
@SuppressWarnings("all")
@NoArgsConstructor
public class ArtPreAuthenticatedUserDetailsService<T extends Authentication>
		implements AuthenticationUserDetailsService<T>, InitializingBean {

	/**
	 * 客户端ID和用户服务 UserDetailService 的映射
	 */
	private Map<String, ArtUserDetailsService> userDetailsServiceMap;

	public ArtPreAuthenticatedUserDetailsService(Map<String, ArtUserDetailsService> userDetailsServiceMap) {
		Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
		this.userDetailsServiceMap = userDetailsServiceMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsServiceMap, "UserDetailsService must be set");
	}

	/**
	 * 重写PreAuthenticatedAuthenticationProvider 的 preAuthenticatedUserDetailsService 属性，
	 * 可根据客户端和认证方式选择用户服务 UserDetailService 获取用户信息 UserDetail
	 * @param authentication
	 * @return userDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
		// 获取客户端id
		String clientId = SecurityUtil.getOAuth2ClientId();
		log.info("clientId:{}", clientId);

		// 根据客户端id获取获取UserDetailsService
		ArtUserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);

		Optional<ArtUserDetailsService> fxzUserDetailsService = userDetailsServiceMap.values()
			.stream()
			.filter(s -> s.support(clientId, null))
			.findFirst();

		if (!fxzUserDetailsService.isPresent()) {
			throw new FxzException("请检查客户端配置");
		}

		return fxzUserDetailsService.get().loadUserByUsername(authentication.getName());
	}

}
