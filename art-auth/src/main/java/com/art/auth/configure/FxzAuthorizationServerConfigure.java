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

package com.art.auth.configure;

import com.art.auth.translator.FxzWebResponseExceptionTranslator;
import com.art.common.core.constant.SecurityConstants;
import com.art.common.security.entity.FxzAuthUser;
import com.art.common.security.extension.captcha.FxzCaptchaTokenGranter;
import com.art.common.security.extension.mobile.FxzSmsCodeTokenGranter;
import com.art.common.security.properties.FxzCloudSecurityProperties;
import com.art.common.security.service.FxzPreAuthenticatedUserDetailsService;
import com.art.common.security.service.FxzUserDetailsService;
import com.art.common.security.service.user.FxzUserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.util.*;

/**
 * 认证服务器配置
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-27 16:10
 */
@SuppressWarnings("rawtypes")
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class FxzAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

	private final DataSource dataSource;

	private final RedisTemplate redisTemplate;

	private final AuthenticationManager authenticationManager;

	private final RedisConnectionFactory redisConnectionFactory;

	private final Map<String, FxzUserDetailsService> userDetailsServiceMap;

	private final FxzUserDetailServiceImpl fxzUserDetailService;

	private final FxzCloudSecurityProperties fxzSecurityProperties;

	private final FxzWebResponseExceptionTranslator fxzWebResponseExceptionTranslator;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		// 允许表单认证
		security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(fxzClientDetailsService());
	}

	@Override
	@SuppressWarnings("all")
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
		List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

		// 添加验证码授权模式授权者
		granterList.add(new FxzCaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory(), authenticationManager, redisTemplate));

		// 添加手机短信验证码授权模式的授权者
		granterList.add(new FxzSmsCodeTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory(), authenticationManager));

		CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);

		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancer()).authenticationManager(authenticationManager)
				.tokenServices(defaultTokenServices()).pathMapping("/oauth/confirm_access", "/token/confirm_access")
				.tokenGranter(compositeTokenGranter).exceptionTranslator(fxzWebResponseExceptionTranslator);
	}

	/**
	 * 客户端信息加载处理
	 * @return ClientDetailsService
	 */
	@Bean
	public ClientDetailsService fxzClientDetailsService() {
		JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
		clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
		return clientDetailsService;
	}

	@Bean
	public TokenStore tokenStore() {
		RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
		redisTokenStore.setPrefix(SecurityConstants.TOKEN_PREFIX);
		return redisTokenStore;
	}

	@Bean
	public UserAuthenticationConverter userAuthenticationConverter() {
		DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
		defaultUserAuthenticationConverter.setUserDetailsService(fxzUserDetailService);
		return defaultUserAuthenticationConverter;
	}

	@Primary
	@Bean
	public DefaultTokenServices defaultTokenServices() {
		// 为DefaultTokenServices设置七大参数
		DefaultTokenServices tokenServices = new DefaultTokenServices();

		// token采用redis存储
		tokenServices.setTokenStore(tokenStore());
		// refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
		// 1 重复使用：access_token过期刷新时， refresh_token过期时间未改变，仍以初次生成的 时间为准
		// 2 非重复使用：access_token过期刷新时，
		// refresh_token过期时间延续，在refresh_token有效期内刷新便永不失效达到无需再次登录的目的
		tokenServices.setSupportRefreshToken(true);
		// token增强，添加了字段
		tokenServices.setTokenEnhancer(tokenEnhancer());
		// token令牌有效时间
		tokenServices.setAccessTokenValiditySeconds(fxzSecurityProperties.getAccessTokenValiditySeconds());
		// 刷新token有效时间
		tokenServices.setRefreshTokenValiditySeconds(fxzSecurityProperties.getRefreshTokenValiditySeconds());
		// 设置ClientDetailsService
		tokenServices.setClientDetailsService(fxzClientDetailsService());

		// 重写预认证提供者替换其AuthenticationManager，可自定义根据客户端ID和认证方式区分用户体系获取认证用户信息
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(
				new FxzPreAuthenticatedUserDetailsService<>(userDetailsServiceMap));

		tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));

		return tokenServices;
	}

	/**
	 * token 生成接口输出增强
	 * @return TokenEnhancer
	 */
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return (accessToken, authentication) -> {
			final Map<String, Object> additionalInfo = new HashMap<>(8);
			String clientId = authentication.getOAuth2Request().getClientId();
			additionalInfo.put(SecurityConstants.CLIENT_ID, clientId);
			additionalInfo.put(SecurityConstants.ACTIVE, Boolean.TRUE);

			FxzAuthUser user = (FxzAuthUser) authentication.getUserAuthentication().getPrincipal();
			additionalInfo.put(SecurityConstants.DETAILS_USER_ID, user.getUserId());
			additionalInfo.put(SecurityConstants.DETAILS_USERNAME, user.getUsername());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
			return accessToken;
		};
	}

}
