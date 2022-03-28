package com.fxz.common.security.config;

import com.fxz.common.core.interceptor.FxzServerProtectInterceptor;
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
	public HandlerInterceptor fxzServerProtectInterceptor() {
		return new FxzServerProtectInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(fxzServerProtectInterceptor());
	}

}
