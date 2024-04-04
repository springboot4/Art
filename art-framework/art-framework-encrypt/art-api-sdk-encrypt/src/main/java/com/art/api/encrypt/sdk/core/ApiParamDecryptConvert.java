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

package com.art.api.encrypt.sdk.core;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.art.api.encrypt.sdk.annotation.ApiDecrypt;
import com.art.api.encrypt.sdk.config.ApiEncryptProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2024/4/4 15:50
 */
@RequiredArgsConstructor
public class ApiParamDecryptConvert implements ApiDecryptConvert, HandlerMethodArgumentResolver {

	private final ApiEncryptProperties apiEncryptProperties;

	@Nullable
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		// 对于GET请求来说是: ?requestParameter=xxx 其中requestParameter就是key xxx就是加密后的请求参数
		String requestParameter = webRequest.getParameter(apiEncryptProperties.getEncryptionKey());
		if (!StringUtils.hasText(requestParameter)) {
			return null;
		}

		// 解密请求参数
		byte[] decryptRequestParameter = decryptRequestParameter(requestParameter,
				apiEncryptProperties.getEncryptType(), apiEncryptProperties.getKey());
		JSONObject jsonObject = JSONUtil.parseObj(decryptRequestParameter);

		// 获取方法参数、注解信息
		Parameter methodParameter = parameter.getParameter();
		ApiDecrypt apiDecrypt = AnnotatedElementUtils.getMergedAnnotation(methodParameter, ApiDecrypt.class);
		Objects.requireNonNull(apiDecrypt, "The ApiDecrypt annotation is not configured");

		// @ApiDecrypt(parameter = "parameterName") 优先级高于方法参数名
		String parameterName = methodParameter.getName();
		if (StringUtils.hasText(apiDecrypt.parameter())) {
			parameterName = apiDecrypt.parameter();
		}

		return jsonObject.get(parameterName, methodParameter.getType());
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return AnnotatedElementUtils.hasAnnotation(parameter.getParameter(), ApiDecrypt.class);
	}

}
