package com.fxz.auth.configure;

import com.common.handler.FxzAccessDeniedHandler;
import com.common.handler.FxzAuthExceptionEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:05
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class FxzResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final FxzAccessDeniedHandler fxzAccessDeniedHandler;

    private final FxzAuthExceptionEntryPoint fxzAuthExceptionEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //该安全配置对所有请求都生效
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers("/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(fxzAuthExceptionEntryPoint)
                .accessDeniedHandler(fxzAccessDeniedHandler);
    }
}
