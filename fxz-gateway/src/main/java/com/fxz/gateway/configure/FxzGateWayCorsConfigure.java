package com.fxz.gateway.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 跨域设置
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-12-07 11:34
 */
@Configuration
public class FxzGateWayCorsConfigure {

	@Bean
	public CorsWebFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
		CorsConfiguration cors = new CorsConfiguration();
		cors.setAllowCredentials(true);
		cors.addAllowedOrigin(CorsConfiguration.ALL);
		cors.addAllowedHeader(CorsConfiguration.ALL);
		cors.addAllowedMethod(CorsConfiguration.ALL);
		source.registerCorsConfiguration("/**", cors);
		return new CorsWebFilter(source);
	}

}
