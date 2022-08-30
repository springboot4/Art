package com.fxz.common.security.config;

import com.fxz.common.security.interceptor.FxzServerProtectInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Fxz
 * @version 1.0
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
