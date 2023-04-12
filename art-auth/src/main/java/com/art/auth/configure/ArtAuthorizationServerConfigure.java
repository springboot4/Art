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

import com.art.common.security.authorization.AuthenticationProvidersCustomCustomizer;
import com.art.common.security.authorization.OAuth2ClientAuthenticationConfigurerCustomizer;
import com.art.common.security.authorization.OAuth2TokenEndpointConfigurerCustomizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 认证服务器配置
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023-4-11
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ArtAuthorizationServerConfigure {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	/**
	 * 授权服务过滤器链 匹配授权端点请求
	 *
	 * @see OAuth2AuthorizationServerConfigurer#getEndpointsMatcher
	 */
	@Bean
	@Order(-1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		// 应用默认OAuth2AuthorizationServerConfigurer配置
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		// 获取authorizationServerConfigurer
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
			.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

		// 配置授权服务器
		authorizationServerConfigurer
			// tokenEndpoint配置
			.tokenEndpoint(new OAuth2TokenEndpointConfigurerCustomizer())
			// 客户端认证配置
			.clientAuthentication(new OAuth2ClientAuthenticationConfigurerCustomizer())
			// 授权服务
			.authorizationService(oAuth2AuthorizationService)
			// 授权服务器设置
			.authorizationServerSettings(AuthorizationServerSettings.builder().issuer("https://art.com").build());

		DefaultSecurityFilterChain securityFilterChain = http.build();

		// 添加自定义的各种认证类型
		new AuthenticationProvidersCustomCustomizer().customize(http);

		return securityFilterChain;
	}

	/**
	 * todo 对于客户端信息 我们应该使用数据库存储，此部分内容放在核心包中 1: 需要建表存储客户端信息 2: 对于查询应该为jdbc，@see
	 * JdbcRegisteredClientRepository.class 3: 对于客户端应该提供前端可视化的管理
	 * @return RegisteredClientRepository
	 */
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient = RegisteredClient.withId("fxz")
			.clientId("fxz")
			.clientSecret("{noop}123456")
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.authorizationGrantType(AuthorizationGrantType.PASSWORD)
			.redirectUri("http://127.0.0.1:3000/login/oauth2/code/messaging-client-oidc")
			.redirectUri("http://127.0.0.1:3000/authorized")
			.scope(OidcScopes.OPENID)
			.scope(OidcScopes.PROFILE)
			.scope("server")
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
			.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
			.build();

		return new InMemoryRegisteredClientRepository(registeredClient);
	}

}
