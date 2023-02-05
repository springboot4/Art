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

package com.art.gateway.util;

import com.alibaba.fastjson.JSONObject;
import com.art.common.core.model.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-04-07 15:25
 */
public class WebFluxUtil {

	/**
	 * 设置webflux模型响应
	 * @param response ServerHttpResponse
	 * @param value 响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value) {
		return webFluxResponseWriter(response, HttpStatus.OK, value, 500);
	}

	/**
	 * 设置webflux模型响应
	 * @param response ServerHttpResponse
	 * @param code 响应状态码
	 * @param value 响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code) {
		return webFluxResponseWriter(response, HttpStatus.OK, value, code);
	}

	/**
	 * 设置webflux模型响应
	 * @param response ServerHttpResponse
	 * @param status http状态码
	 * @param code 响应状态码
	 * @param value 响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status, Object value,
			int code) {
		return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
	}

	/**
	 * 设置webflux模型响应
	 * @param response ServerHttpResponse
	 * @param contentType content-type
	 * @param status http状态码
	 * @param code 响应状态码
	 * @param value 响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status,
			Object value, int code) {
		response.setStatusCode(status);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
		Result<?> result = Result.failed(code, value.toString());
		DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(result).getBytes());
		return response.writeWith(Mono.just(dataBuffer));
	}

}
