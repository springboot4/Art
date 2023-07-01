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

package com.art.common.security.client.config;

import com.art.common.security.client.DefaultGiteeRegisteredClientRepository;
import com.art.common.security.client.GiteeRegisteredClientRepository;
import com.art.common.security.client.http.GiteeAuthorizeHttpFilter;
import com.art.common.security.client.http.GiteeCodeHttpFilter;
import com.art.common.security.client.properties.GiteeProperties;
import com.art.system.api.gitee.UserGiteeServiceApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * gitee oauth2 client相关拦截器
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/15 14:41
 */
@SuppressWarnings("all")
@EnableConfigurationProperties(GiteeProperties.class)
@Configuration
public class ArtGiteeOAuth2ClientConfigure {

	@Bean
	public DefaultGiteeRegisteredClientRepository giteeRegisteredClientRepository(GiteeProperties giteeProperties,
			StringRedisTemplate redisTemplate, UserGiteeServiceApi userGiteeServiceApi) {
		return new DefaultGiteeRegisteredClientRepository(giteeProperties, redisTemplate, userGiteeServiceApi);
	}

	@Bean
	public FilterRegistrationBean<GiteeAuthorizeHttpFilter> giteeAuthorizeHttpFilterFilterRegistrationBean(
			GiteeRegisteredClientRepository giteeRegisteredClientRepository,
			OAuth2AuthorizationService authorizationService) {
		FilterRegistrationBean<GiteeAuthorizeHttpFilter> registration = new FilterRegistrationBean<>();
		GiteeAuthorizeHttpFilter giteeAuthorizeHttpFilter = new GiteeAuthorizeHttpFilter(
				giteeRegisteredClientRepository, authorizationService);
		registration.setFilter(giteeAuthorizeHttpFilter);
		registration.addUrlPatterns("/gitee/authorize/*");
		return registration;
	}

	@Bean
	public FilterRegistrationBean<GiteeCodeHttpFilter> giteeCodeHttpFilterFilterRegistrationBean(
			GiteeRegisteredClientRepository giteeRegisteredClientRepository) {
		FilterRegistrationBean<GiteeCodeHttpFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new GiteeCodeHttpFilter(giteeRegisteredClientRepository));
		registration.addUrlPatterns("/gitee/code/*");
		return registration;
	}

}
