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

import cn.hutool.core.util.StrUtil;
import com.art.common.security.core.client.ArtRegisteredClientRepository;
import com.art.common.security.core.handler.ArtAccessDeniedHandler;
import com.art.common.security.core.handler.ArtAuthExceptionEntryPoint;
import com.art.common.security.core.support.ArtBearerTokenResolver;
import com.art.common.security.core.support.PermissionService;
import com.art.common.security.core.support.RedisOAuth2AuthorizationService;
import com.art.common.security.core.support.SecurityInnerAspect;
import com.art.core.common.constant.ArtConstants;
import com.art.core.common.constant.SecurityConstants;
import com.art.core.common.util.WebUtil;
import com.art.json.sdk.module.JavaTimeModule;
import com.art.system.api.client.ClientServiceApi;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
	RedisOAuth2AuthorizationService oAuth2AuthorizationService(
			RedisTemplate<String, OAuth2Authorization> oauth2RedisTemplate) {
		return new RedisOAuth2AuthorizationService(oauth2RedisTemplate);
	}

	@Bean
	public RedisTemplate<String, OAuth2Authorization> oauth2RedisTemplate(LettuceConnectionFactory factory) {
		// 创建 RedisTemplate 对象
		RedisTemplate<String, OAuth2Authorization> template = new RedisTemplate<>();
		// 设置 RedisConnection 工厂。
		template.setConnectionFactory(factory);

		ObjectMapper objectMapper = new ObjectMapper();
		// 指定要序列化的域
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
			// 不将日期写为时间戳
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			// 忽略未知属性
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			// 对象属性为空时可以序列化
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
			// 记录被序列化的类型信息
			.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
					JsonTypeInfo.As.WRAPPER_ARRAY)
			// null 值不序列化
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			// 日期处理
			.registerModule(new JavaTimeModule());
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		// 默认使用了jdk的序列化方式，可读性差，我们使用 String 序列化方式，序列化 KEY 。
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());

		// 使用 Jdk 序列化方式。
		template.setValueSerializer(RedisSerializer.java());
		template.setHashValueSerializer(RedisSerializer.java());

		return template;
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
			String gatewayToken = new String(Base64Utils.encode((ArtConstants.GATEWAY_TOKEN_VALUE).getBytes()));
			requestTemplate.header(ArtConstants.GATEWAY_TOKEN_HEADER, gatewayToken);

			HttpServletRequest request = WebUtil.getRequest();
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
