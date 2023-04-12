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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.CollectionUtils;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;

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

	@Bean
	@Order(-1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
			.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

		authorizationServerConfigurer.tokenEndpoint(tokenEndpointCustomizer -> {
			tokenEndpointCustomizer
				// 注入自定义的授权认证Converter
				.accessTokenRequestConverter(new DelegatingAuthenticationConverter(
						Arrays.asList(new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
								new OAuth2RefreshTokenAuthenticationConverter(),
								new OAuth2ClientCredentialsAuthenticationConverter(),
								new OAuth2AuthorizationCodeAuthenticationConverter(),
								new OAuth2AuthorizationCodeRequestAuthenticationConverter())))
				// 登录成功处理器
				.accessTokenResponseHandler((request, response, authentication) -> {
					log.info("登录成功:{}", authentication);

					HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
					OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

					OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
					OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
					Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

					OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse
						.withToken(accessToken.getTokenValue())
						.tokenType(accessToken.getTokenType())
						.scopes(accessToken.getScopes());

					if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
						builder.expiresIn(
								ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
					}
					if (refreshToken != null) {
						builder.refreshToken(refreshToken.getTokenValue());
					}
					if (!CollectionUtils.isEmpty(additionalParameters)) {
						builder.additionalParameters(additionalParameters);
					}
					OAuth2AccessTokenResponse accessTokenResponse = builder.build();

					ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

					SecurityContextHolder.clearContext();
					accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
				})
				// 登录失败处理器
				.errorResponseHandler((request, response, exception) -> {
					log.info("登录失败:{}", exception.getLocalizedMessage());
				});
		})
			.clientAuthentication(oAuth2ClientAuthenticationConfigurer -> oAuth2ClientAuthenticationConfigurer
				.errorResponseHandler((request, response, exception) -> {
					log.info("登录失败:{}", exception.getLocalizedMessage());
				}))
			.authorizationService(new InMemoryOAuth2AuthorizationService())
			.authorizationServerSettings(AuthorizationServerSettings.builder().issuer("https://art.com").build());

		DefaultSecurityFilterChain securityFilterChain = http.build();

		customAuthenticationProviders(http);

		return securityFilterChain;
	}

	private void customAuthenticationProviders(HttpSecurity http) {
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

		http.authenticationProvider(new DaoAuthenticationProvider());
		http.authenticationProvider(new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager,
				authorizationService, new DelegatingOAuth2TokenGenerator(new OAuth2AccessTokenGenerator(),
						new OAuth2RefreshTokenGenerator())));
	}

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
