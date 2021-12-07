package com.fxz.fxzregister.configure;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author fxz
 */
@EnableWebSecurity
public class FxzRegisterWebSecurityConfigure extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().ignoringAntMatchers("/eureka/**").and().authorizeRequests().antMatchers("/actuator/**").permitAll();
		;
		super.configure(http);
	}

}