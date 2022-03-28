package com.fxz.auth.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/token/login").loginProcessingUrl("/token/form").and().requestMatchers()
				.antMatchers("/token/**", "/oauth/**").and().authorizeRequests().antMatchers("/token/**").permitAll()
				.anyRequest().authenticated().and().csrf().disable();
	}

	/**
	 * 认证中心静态资源处理
	 * @param web WebSecurity
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**");
	}

}
