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
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.art.api.encrypt.sdk.annotation.ApiDecrypt;
import com.art.api.encrypt.sdk.config.ApiEncryptProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2024/4/4 16:21
 */
@Order(1)
@ControllerAdvice
@RequiredArgsConstructor
public class ApiRequestBodyDecryptConvert implements ApiDecryptConvert, RequestBodyAdvice {

	private final ApiEncryptProperties apiEncryptProperties;

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		return new HttpInputMessage() {

			@Override
			public HttpHeaders getHeaders() {
				return inputMessage.getHeaders();
			}

			@Override
			public InputStream getBody() throws IOException {
				// 获取请求的内容
				InputStream body = inputMessage.getBody();
				if (body.available() <= 0) {
					return body;
				}

				// JSON 解析请求中的内容 {encryptionKey: encryptionBody}
				JSONObject jsonObject = JSONUtil.parseObj(IoUtil.readUtf8(body));
				String encryptionBody = (String) jsonObject.get(apiEncryptProperties.getEncryptionKey());
				if (!StringUtils.hasText(encryptionBody)) {
					throw new UnsupportedOperationException("Encryption body is empty");
				}

				// 解密请求体
				byte[] decryptByte = decryptRequestParameter(encryptionBody, apiEncryptProperties.getEncryptType(),
						apiEncryptProperties.getKey());

				return new ByteArrayInputStream(decryptByte);
			}
		};
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return AnnotationUtil.hasAnnotation(methodParameter.getMethod(), ApiDecrypt.class);
	}

}
