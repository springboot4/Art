package com.fxz.serversystem.configure;

import com.fxz.common.security.FxzUserInfoTokenServices;
import com.fxz.common.security.handler.FxzAccessDeniedHandler;
import com.fxz.common.security.handler.FxzAuthExceptionEntryPoint;
import com.fxz.serversystem.properties.FxzServerSystemProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 17:55
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class FxzServerSystemResourceServerConfigure extends ResourceServerConfigurerAdapter {

	private final ResourceServerProperties sso;

	private final FxzAccessDeniedHandler fxzAccessDeniedHandler;

	private final FxzAuthExceptionEntryPoint fxzAuthExceptionEntryPoint;

	private final FxzServerSystemProperties properties;

	@Bean
	public FxzUserInfoTokenServices fxzUserInfoTokenServices() {
		return new FxzUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

		http.csrf().disable().requestMatchers().antMatchers("/**").and().authorizeRequests().antMatchers(anonUrls)
				.permitAll().antMatchers("/**").authenticated();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.authenticationEntryPoint(fxzAuthExceptionEntryPoint).accessDeniedHandler(fxzAccessDeniedHandler)
				.tokenServices(fxzUserInfoTokenServices());
	}

}
