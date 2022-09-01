package com.fxz.common.security.service;

import com.fxz.common.core.exception.FxzException;
import com.fxz.common.security.util.SecurityUtil;
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
public class FxzPreAuthenticatedUserDetailsService<T extends Authentication>
		implements AuthenticationUserDetailsService<T>, InitializingBean {

	/**
	 * 客户端ID和用户服务 UserDetailService 的映射
	 */
	private Map<String, FxzUserDetailsService> userDetailsServiceMap;

	public FxzPreAuthenticatedUserDetailsService(Map<String, FxzUserDetailsService> userDetailsServiceMap) {
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
		FxzUserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);

		Optional<FxzUserDetailsService> fxzUserDetailsService = userDetailsServiceMap.values().stream()
				.filter(s -> s.support(clientId, null)).findFirst();

		if (!fxzUserDetailsService.isPresent()) {
			throw new FxzException("请检查客户端配置");
		}

		return fxzUserDetailsService.get().loadUserByUsername(authentication.getName());
	}

}
