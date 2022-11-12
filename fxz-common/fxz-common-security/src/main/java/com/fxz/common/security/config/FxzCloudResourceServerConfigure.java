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

package com.fxz.common.security.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fxz.common.security.constant.EndpointConstant;
import com.fxz.common.security.handler.FxzAccessDeniedHandler;
import com.fxz.common.security.handler.FxzAuthExceptionEntryPoint;
import com.fxz.common.security.properties.FxzCloudSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-03-06 18:10
 */
@RequiredArgsConstructor
@EnableResourceServer
public class FxzCloudResourceServerConfigure extends ResourceServerConfigurerAdapter {

	/**
	 * 资源服务器属性配置
	 */
	private final FxzCloudSecurityProperties properties;

	/**
	 * 用于处理403类型异常
	 */
	private final FxzAccessDeniedHandler fxzAccessDeniedHandler;

	/**
	 * 用于处理401类型异常
	 */
	private final FxzAuthExceptionEntryPoint fxzAuthExceptionEntryPoint;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 如果资源服务器配置为空 放行所有请求
		if (properties == null) {
			permitAll(http);
			return;
		}
		// 放行配置文件中指定的请求
		String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUris(),
				StringPool.COMMA);
		if (ArrayUtils.isEmpty(anonUrls)) {
			anonUrls = new String[] {};
		}
		if (ArrayUtils.contains(anonUrls, EndpointConstant.ALL)) {
			permitAll(http);
			return;
		}
		http.csrf().disable().requestMatchers().antMatchers(properties.getAuthUri()).and().authorizeRequests()
				.antMatchers(anonUrls).permitAll().antMatchers(properties.getAuthUri()).authenticated().and()
				.httpBasic();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.authenticationEntryPoint(fxzAuthExceptionEntryPoint).accessDeniedHandler(fxzAccessDeniedHandler);
	}

	private void permitAll(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().permitAll();
	}

}
