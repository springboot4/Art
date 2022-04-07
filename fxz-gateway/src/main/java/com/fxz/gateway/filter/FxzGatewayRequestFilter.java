package com.fxz.gateway.filter;

import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.mp.result.Result;
import com.fxz.gateway.properties.FxzGatewayProperties;
import com.fxz.gateway.util.WebFluxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
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

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 黑名单 不允许访问的地址
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-12-07 10:18
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FxzGatewayRequestFilter implements GlobalFilter {

	private final FxzGatewayProperties properties;

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		// 禁止客户端的访问资源逻辑
		Mono<Void> checkForbidUriResult = checkForbidUri(request, response);
		if (checkForbidUriResult != null) {
			return checkForbidUriResult;
		}

		// 日志打印
		printLog(exchange);

		byte[] token = Base64Utils.encode((FxzConstant.GATEWAY_TOKEN_VALUE).getBytes());
		ServerHttpRequest build = request.mutate().header(FxzConstant.GATEWAY_TOKEN_HEADER, new String(token)).build();
		return chain.filter(exchange.mutate().request(build).build());
	}

	private Mono<Void> checkForbidUri(ServerHttpRequest request, ServerHttpResponse response) {
		String uri = request.getPath().toString();
		boolean shouldForward = true;
		String forbidRequestUri = properties.getForbidRequestUri();
		String[] forbidRequestUris = StringUtils.splitByWholeSeparatorPreserveAllTokens(forbidRequestUri, ",");
		if (ArrayUtils.isNotEmpty(forbidRequestUris)) {
			for (String u : forbidRequestUris) {
				if (pathMatcher.match(u, uri)) {
					shouldForward = false;
				}
			}
		}
		if (!shouldForward) {
			return WebFluxUtil.webFluxResponseWriter(response, Result.failed("该URI不允许外部访问"),
					HttpStatus.FORBIDDEN.value());
		}
		return null;
	}

	private void printLog(ServerWebExchange exchange) {
		URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
		LinkedHashSet<URI> uris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
		URI originUri = null;
		if (uris != null) {
			originUri = uris.stream().findFirst().orElse(null);
		}
		if (url != null && route != null && originUri != null) {
			log.info("转发请求：{}://{}{} --> 目标服务：{}，目标地址：{}://{}{}，转发时间：{}", originUri.getScheme(),
					originUri.getAuthority(), originUri.getPath(), route.getId(), url.getScheme(), url.getAuthority(),
					url.getPath(), LocalDateTime.now());
		}
	}

}
