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

import cn.hutool.core.annotation.AnnotationUtil;
import com.art.api.encrypt.sdk.annotation.ApiEncrypt;
import com.art.api.encrypt.sdk.config.ApiEncryptProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2024/4/4 16:52
 */
@Order(1)
@ControllerAdvice
@RequiredArgsConstructor
public class ApiResponseBodyEncryptConvert implements ApiEncryptConvert, ResponseBodyAdvice<Object> {

	private final ApiEncryptProperties apiEncryptProperties;

	private final ObjectMapper objectMapper;

	@Nullable
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (Objects.isNull(body)) {
			return body;
		}

		String encryptData = null;
		try {
			encryptData = encryptData(objectMapper.writeValueAsBytes(body), apiEncryptProperties.getEncryptType(),
					apiEncryptProperties.getKey());
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		Map<String, Object> data = new HashMap<>(2);
		data.put(apiEncryptProperties.getKey(), encryptData);
		return data;
	}

	@Nullable
	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return AnnotationUtil.hasAnnotation(returnType.getMethod(), ApiEncrypt.class);
	}

}
