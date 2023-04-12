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

import com.art.common.security.authentication.DaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 开启和Web相关的安全配置
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023-4-11
 */
@EnableWebSecurity
public class ArtSecurityConfigure {

	/**
	 * 默认安全策略过滤器链
	 */
	@Bean
	@Order(0)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(
				authorizeRequests -> authorizeRequests.antMatchers("/token/*").permitAll().anyRequest().authenticated())
			.headers()
			.frameOptions()
			.sameOrigin();

		http.authenticationProvider(new DaoAuthenticationProvider());
		return http.build();
	}

	@Bean
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
		http.requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
			.authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
			.requestCache()
			.disable()
			.securityContext()
			.disable()
			.sessionManagement()
			.disable();
		return http.build();
	}

}
