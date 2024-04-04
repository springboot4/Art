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

package com.art.api.encrypt.starter;

import com.art.api.encrypt.sdk.config.ApiEncryptProperties;
import com.art.api.encrypt.sdk.core.ApiParamDecryptConvert;
import com.art.api.encrypt.sdk.core.ApiRequestBodyDecryptConvert;
import com.art.api.encrypt.sdk.core.ApiResponseBodyEncryptConvert;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2024/4/4 17:14
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiEncryptProperties.class)
@AutoConfiguration
public class ApiEncryptAutoConfiguration implements WebMvcConfigurer {

	private final ApiEncryptProperties apiEncryptProperties;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new ApiParamDecryptConvert(apiEncryptProperties));
	}

	@Bean
	public ApiRequestBodyDecryptConvert apiRequestBodyDecryptConvert(ApiEncryptProperties apiEncryptProperties) {
		return new ApiRequestBodyDecryptConvert(apiEncryptProperties);
	}

	@Bean
	public ApiResponseBodyEncryptConvert apiResponseBodyEncryptConvert(ApiEncryptProperties apiEncryptProperties,
			ObjectMapper objectMapper) {
		return new ApiResponseBodyEncryptConvert(apiEncryptProperties, objectMapper);
	}

}
