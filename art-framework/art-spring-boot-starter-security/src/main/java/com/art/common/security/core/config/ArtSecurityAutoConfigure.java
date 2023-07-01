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

package com.art.common.security.core.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.art.common.security.core.client.ArtRegisteredClientRepository;
import com.art.common.security.core.handler.ArtAccessDeniedHandler;
import com.art.common.security.core.handler.ArtAuthExceptionEntryPoint;
import com.art.common.security.core.support.ArtBearerTokenResolver;
import com.art.common.security.core.support.PermissionService;
import com.art.common.security.core.support.RedisOAuth2AuthorizationService;
import com.art.common.security.core.support.SecurityInnerAspect;
import com.art.core.common.constant.FxzConstant;
import com.art.core.common.constant.SecurityConstants;
import com.art.core.common.util.WebUtil;
import com.art.system.api.client.ClientServiceApi;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-06 18:15<br/>
 * 开启了基于注解的权限控制{@link EnableGlobalMethodSecurity}
 */
@AutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(ArtSecurityProperties.class)
public class ArtSecurityAutoConfigure {

	@SuppressWarnings("all")
	@Bean
	ArtRegisteredClientRepository artRegisteredClientRepository(ClientServiceApi clientServiceApi) {
		return new ArtRegisteredClientRepository(clientServiceApi);
	}

	@Bean
	RedisOAuth2AuthorizationService oAuth2AuthorizationService(RedisTemplate<String, Object> redisTemplate) {
		return new RedisOAuth2AuthorizationService(redisTemplate);
	}

	@Bean
	BearerTokenResolver bearerTokenResolver(ArtSecurityProperties properties) {
		return new ArtBearerTokenResolver(properties);
	}

	/**
	 * 注入密码编码器
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * 接口权限判断工具
	 */
	@Bean("ps")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	/**
	 * 权限切面判断
	 */
	@Bean
	public SecurityInnerAspect securityInnerAspect(HttpServletRequest request) {
		return new SecurityInnerAspect(request);
	}

	/**
	 * 用于处理403类型异常
	 */
	@Primary
	@Bean
	public ArtAccessDeniedHandler accessDeniedHandler() {
		return new ArtAccessDeniedHandler();
	}

	/**
	 * 用于处理401类型异常
	 */
	@Primary
	@Bean
	public ArtAuthExceptionEntryPoint authenticationEntryPoint() {
		return new ArtAuthExceptionEntryPoint();
	}

	/**
	 * 为feign请求头添加令牌
	 */
	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor(ArtSecurityProperties properties) {
		return requestTemplate -> {
			String gatewayToken = Base64.encode((FxzConstant.GATEWAY_TOKEN_VALUE).getBytes());
			requestTemplate.header(FxzConstant.GATEWAY_TOKEN_HEADER, gatewayToken);
			jakarta.servlet.http.HttpServletRequest request = WebUtil.getReq();
			String token = bearerTokenResolver(properties).resolve(request);
			if (StrUtil.isBlank(token)) {
				return;
			}
			requestTemplate.header(HttpHeaders.AUTHORIZATION,
					String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token));

			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			if (requestAttributes != null) {
				ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
				request = attributes.getRequest();
				// 获取请求头
				Enumeration<String> headerNames = request.getHeaderNames();
				if (headerNames != null) {
					while (headerNames.hasMoreElements()) {
						String name = headerNames.nextElement();
						String values = request.getHeader(name);
						if (name.equals(HttpHeaders.AUTHORIZATION.toLowerCase())
								&& values.contains(SecurityConstants.BASIC_PREFIX.trim())
								|| name.equals(HttpHeaders.CONTENT_LENGTH.toLowerCase())) {
							continue;
						}
						// 将请求头保存到模板中
						requestTemplate.header(name, values);
					}
				}
			}
		};
	}

}
