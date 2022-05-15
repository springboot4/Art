package com.fxz.auth.service;

import com.fxz.common.core.constant.SecurityConstants;
import com.fxz.common.security.util.SecurityUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 认证前，根据客户端id选择UserDetailsService
 *
 * @author fxz
 */
@Slf4j
@SuppressWarnings("all")
@NoArgsConstructor
public class PreAuthenticatedUserDetailsService<T extends Authentication>
		implements AuthenticationUserDetailsService<T>, InitializingBean {

	/**
	 * 客户端ID和用户服务 UserDetailService 的映射
	 */
	private Map<String, UserDetailsService> userDetailsServiceMap;

	public PreAuthenticatedUserDetailsService(Map<String, UserDetailsService> userDetailsServiceMap) {
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
		UserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);

		// 根据客户端选择加载用户的方式
		if (clientId.equals(SecurityConstants.APP_CLIENT_ID)) {
			log.info("移动端");
			// todo 移动端的用户体系是会员，认证方式是通过手机号 mobile 认证，暂时根据用户名密码加载用户信息

			return userDetailsService.loadUserByUsername(authentication.getName());
		}
		else if (clientId.equals(SecurityConstants.WEAPP_CLIENT_ID)) {
			log.info("小程序端");
			// todo 小程序的用户体系是会员，认证方式是通过微信三方标识 openid 认证
			FxzMemberUserDetailsServiceImpl memberUserDetailsService = (FxzMemberUserDetailsServiceImpl) userDetailsService;

			return memberUserDetailsService.loadUserByOpenId(authentication.getName());
		}
		else if (clientId.equals(SecurityConstants.ADMIN_CLIENT_ID)) {
			log.info("系统管理端");
			// 管理系统的用户体系是系统用户，认证方式通过用户名 username 认证
			return userDetailsService.loadUserByUsername(authentication.getName());
		}
		else {
			return userDetailsService.loadUserByUsername(authentication.getName());
		}
	}

}
