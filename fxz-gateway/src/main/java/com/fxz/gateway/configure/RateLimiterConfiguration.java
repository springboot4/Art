package com.fxz.gateway.configure;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RateLimiterConfiguration {

	@Bean
	@Primary
	KeyResolver apiKeyResolver() {
		// 按URL限流,即以每秒内请求数按URL分组统计，超出限流的url请求都将返回429状态
		return exchange -> Mono.just(exchange.getRequest().getPath().toString());
	}

	/**
	 * 网关中根据#SPEL表达式去的对应的bean
	 *
	 * @link {https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory}
	 */
	@Bean
	public KeyResolver remoteAddrKeyResolver() {
		return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName());
	}

}
