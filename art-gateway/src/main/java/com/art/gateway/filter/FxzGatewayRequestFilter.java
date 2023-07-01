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

package com.art.gateway.filter;

import com.art.core.common.constant.FxzConstant;
import com.art.core.common.model.Result;

import com.art.gateway.properties.FxzGatewayProperties;
import com.art.gateway.util.WebFluxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 日志、黑名单过滤器
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2021-12-07 10:18
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FxzGatewayRequestFilter implements GlobalFilter, Ordered {

	private final FxzGatewayProperties properties;

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final String COUNT_START_TIME = "countStartTime";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		// 禁止客户端的访问资源逻辑
		Mono<Void> checkForbidUriResult = checkForbidUri(request, response);
		if (Objects.nonNull(checkForbidUriResult)) {
			return checkForbidUriResult;
		}

		// 方法计时
		exchange.getAttributes().put(COUNT_START_TIME, System.currentTimeMillis());

		// 标记请求来自网关
		ServerHttpRequest build = request.mutate()
			.header(FxzConstant.GATEWAY_TOKEN_HEADER,
					new String(Base64Utils.encode((FxzConstant.GATEWAY_TOKEN_VALUE).getBytes())))
			.build();

		// then方法相当于aop的后置通知一样
		return chain.filter(exchange.mutate().request(build).build()).then(Mono.fromRunnable(() -> printLog(exchange)));
	}

	/**
	 * 检验url是否禁止访问
	 */
	private Mono<Void> checkForbidUri(ServerHttpRequest request, ServerHttpResponse response) {
		String uri = request.getPath().toString();
		String forbidRequestUri = properties.getForbidRequestUri();
		String[] forbidRequestUris = StringUtils.splitByWholeSeparatorPreserveAllTokens(forbidRequestUri, ",");
		boolean shouldForward = true;

		if (ArrayUtils.isNotEmpty(forbidRequestUris)) {
			for (String u : forbidRequestUris) {
				if (pathMatcher.match(u, uri)) {
					shouldForward = false;
					break;
				}
			}
		}

		if (!shouldForward) {
			return WebFluxUtil.webFluxResponseWriter(response, Result.failed("该URI不允许外部访问"),
					HttpStatus.FORBIDDEN.value());
		}
		return null;
	}

	/**
	 * 打印执行日志
	 */
	private void printLog(ServerWebExchange exchange) {
		URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
		LinkedHashSet<URI> uris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

		Long startTime = exchange.getAttribute(COUNT_START_TIME);
		Long executeTime = (System.currentTimeMillis() - startTime);

		URI originUri = null;
		if (uris != null) {
			originUri = uris.stream().findFirst().orElse(null);
		}

		if (url != null && route != null && originUri != null) {
			log.info("转发请求：{}://{}{} --> 目标服务：{}，目标地址：{}://{}{}，转发时间：{}，方法耗时：{}ms", originUri.getScheme(),
					originUri.getAuthority(), originUri.getPath(), route.getId(), url.getScheme(), url.getAuthority(),
					url.getPath(), LocalDateTime.now(), executeTime);
		}
	}

	/**
	 * 数值越低，过滤器优先级越高
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
