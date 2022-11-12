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

package com.fxz.gateway.handler;

import com.fxz.gateway.util.WebFluxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关异常通用处理器
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-12-07 11:01
 */
@Slf4j
public class FxzGatewayExceptionHandler implements ErrorWebExceptionHandler {

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable fxz) {
		ServerHttpResponse response = exchange.getResponse();

		if (response.isCommitted()) {
			return Mono.error(fxz);
		}

		String msg;

		if (fxz instanceof NotFoundException) {
			msg = "未找到该资源";
		}
		else if (fxz instanceof ResponseStatusException) {
			ResponseStatusException responseStatusException = (ResponseStatusException) fxz;
			msg = responseStatusException.getMessage();
		}
		else {
			msg = "内部服务器错误";
		}

		log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().getPath(), fxz.getMessage());

		return WebFluxUtil.webFluxResponseWriter(response, msg);
	}

}
