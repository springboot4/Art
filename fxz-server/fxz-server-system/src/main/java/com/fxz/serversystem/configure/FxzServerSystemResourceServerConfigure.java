package com.fxz.serversystem.configure;

import com.common.component.FxzUserAuthenticationConverter;
import com.common.handler.FxzAccessDeniedHandler;
import com.common.handler.FxzAuthExceptionEntryPoint;
import com.fxz.serversystem.properties.FxzAuthProperties;
import com.fxz.serversystem.properties.FxzClientsProperties;
import com.fxz.serversystem.properties.FxzServerSystemProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 17:55
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class FxzServerSystemResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final FxzAuthProperties authProperties;

    private final RemoteTokenServices remoteTokenServices;

    private final FxzAccessDeniedHandler fxzAccessDeniedHandler;

    private final FxzAuthExceptionEntryPoint fxzAuthExceptionEntryPoint;

    private final FxzServerSystemProperties properties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

        http.csrf().disable().requestMatchers().antMatchers("/**").and().authorizeRequests().antMatchers(anonUrls)
                .permitAll().antMatchers("/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        FxzClientsProperties client = authProperties.getClients()[0];
        remoteTokenServices.setClientId(client.getClient());
        remoteTokenServices.setClientSecret(passwordEncoder.encode(client.getSecret()));
        resources.authenticationEntryPoint(fxzAuthExceptionEntryPoint).accessDeniedHandler(fxzAccessDeniedHandler).tokenServices(remoteTokenServices);
    }


}
