package com.fxz.auth.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Map;

/**
 * 开启和Web相关的安全配置
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:00
 */
@Order(1)
@EnableWebSecurity
@RequiredArgsConstructor
public class FxzSecurityConfigure extends WebSecurityConfigurerAdapter {

	private final Map<String, AuthenticationProvider> authenticationProviderMap;

	/**
	 * 必须注入 AuthenticationManager，不然oauth 无法处理四种授权方式
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/token/**", "/oauth/**").and().authorizeRequests().antMatchers("/oauth/**")
				.authenticated().and().formLogin().loginPage("/token/login").loginProcessingUrl("/token/form")
				.permitAll().and().csrf().disable();
	}

	/**
	 * 认证中心静态资源处理
	 * @param web WebSecurity
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/*.html", "/css/**", "/img/**", "/js/**", "/*.ico", "/resource/**");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) {
		authenticationProviderMap.values().forEach(auth::authenticationProvider);
	}

}
