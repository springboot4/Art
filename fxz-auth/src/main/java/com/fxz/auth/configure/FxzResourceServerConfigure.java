package com.fxz.auth.configure;

import com.fxz.auth.properties.FxzAuthProperties;
import com.fxz.common.security.FxzUserInfoTokenServices;
import com.fxz.common.security.handler.FxzAccessDeniedHandler;
import com.fxz.common.security.handler.FxzAuthExceptionEntryPoint;
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
 * @date 2021-11-27 16:05
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class FxzResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final ResourceServerProperties sso;

    private final FxzAuthProperties properties;

    private final FxzAccessDeniedHandler fxzAccessDeniedHandler;

    private final FxzAuthExceptionEntryPoint fxzAuthExceptionEntryPoint;

    @Bean
    public FxzUserInfoTokenServices fxzUserInfoTokenServices() {
        return new FxzUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

        http.csrf().disable()
                // 该安全配置对所有请求都生效
                .requestMatchers().antMatchers("/**").and().authorizeRequests().antMatchers(anonUrls).permitAll()
                .antMatchers("/**").authenticated().and().httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //TODO 配置userInfoTokenService鉴权会失败
        resources.authenticationEntryPoint(fxzAuthExceptionEntryPoint).accessDeniedHandler(fxzAccessDeniedHandler);
    }

}
