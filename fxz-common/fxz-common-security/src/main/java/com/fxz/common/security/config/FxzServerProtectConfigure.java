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

import com.fxz.common.security.interceptor.FxzServerProtectInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 13:14
 */
@SuppressWarnings("all")
public class FxzServerProtectConfigure implements WebMvcConfigurer {

	@Bean
	@ConditionalOnProperty(value = "fxz.cloud.security.onlyFetchByGateway", matchIfMissing = true, havingValue = "true")
	public HandlerInterceptor fxzServerProtectInterceptor() {
		return new FxzServerProtectInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(fxzServerProtectInterceptor());
	}

}
