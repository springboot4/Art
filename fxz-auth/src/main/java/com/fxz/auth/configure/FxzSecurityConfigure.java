package com.fxz.auth.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Fxz
 * @version 1.0
 * @Description 开启和Web相关的安全配置
 * @date 2021-11-27 16:00
 */
@Order(2)
@EnableWebSecurity
@RequiredArgsConstructor
public class FxzSecurityConfigure extends WebSecurityConfigurerAdapter {

	// private final ValidateCodeFilter validateCodeFilter;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// .addFilterBefore(validateCodeFilter,
				// UsernamePasswordAuthenticationFilter.class)
				.requestMatchers()
				// 安全配置类只对/oauth/开头的请求有效。
				.antMatchers("/token/**", "/oauth/**").and().authorizeRequests().antMatchers("/token/**", "/oauth/**")
				.permitAll().and().csrf().disable();
	}

}
