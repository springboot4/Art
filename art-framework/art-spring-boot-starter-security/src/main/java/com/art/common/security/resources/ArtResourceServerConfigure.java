/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.security.resources;

import com.art.common.security.core.config.ArtSecurityProperties;
import com.art.common.security.core.handler.ArtAuthExceptionEntryPoint;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 资源服务器配置
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-06 18:10
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class ArtResourceServerConfigure {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final BearerTokenResolver bearerTokenResolver;

	/**
	 * 资源服务器属性配置
	 */
	private final ArtSecurityProperties properties;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 放行配置文件中指定的请求
		String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUris(),
				StringPool.COMMA);

		http.authorizeRequests(
				authorizeRequests -> authorizeRequests.antMatchers(anonUrls).permitAll().anyRequest().authenticated())
			.oauth2ResourceServer(oauth2 -> oauth2
				// 不透明令牌自省配置
				.opaqueToken(token -> token.introspector(new ArtOpaqueTokenIntrospector(oAuth2AuthorizationService)))
				.authenticationEntryPoint(new ArtAuthExceptionEntryPoint())
				.bearerTokenResolver(bearerTokenResolver))
			.headers()
			.frameOptions()
			.disable()
			.and()
			.csrf()
			.disable();

		return http.build();
	}

}
