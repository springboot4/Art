package com.fxz.auth.configure;

import com.fxz.auth.properties.FxzAuthProperties;
import com.fxz.auth.properties.FxzClientsProperties;
import com.fxz.auth.service.FxzUserDetailServiceImpl;
import com.fxz.auth.translator.FxzWebResponseExceptionTranslator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:10
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class FxzAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;

	private final RedisConnectionFactory redisConnectionFactory;

	private final FxzUserDetailServiceImpl userDetailService;

	private final PasswordEncoder passwordEncoder;

	private final FxzAuthProperties authProperties;

	private final FxzWebResponseExceptionTranslator fxzWebResponseExceptionTranslator;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// 允许表单认证
		security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//todo ClientDetailsService
		FxzClientsProperties[] clientsArray = authProperties.getClients();
		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

		if (ArrayUtils.isNotEmpty(clientsArray)) {
			for (FxzClientsProperties client : clientsArray) {
				if (StringUtils.isBlank(client.getClient())) {
					throw new Exception("client不能为空");
				}
				if (StringUtils.isBlank(client.getSecret())) {
					throw new Exception("secret不能为空");
				}
				String[] grantTypes = StringUtils.splitByWholeSeparatorPreserveAllTokens(client.getGrantType(), ",");
				builder.withClient(client.getClient()).secret(passwordEncoder.encode(client.getSecret()))
						.authorizedGrantTypes(grantTypes).scopes(client.getScope());
			}
		}
	}

	@Override
	@SuppressWarnings("all")
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.tokenStore(tokenStore()).userDetailsService(userDetailService)
				.authenticationManager(authenticationManager).tokenServices(defaultTokenServices())
				.exceptionTranslator(fxzWebResponseExceptionTranslator);
	}

	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	@Bean
	public UserAuthenticationConverter userAuthenticationConverter() {
		DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
		defaultUserAuthenticationConverter.setUserDetailsService(userDetailService);
		return defaultUserAuthenticationConverter;
	}

	@Primary
	@Bean
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setAccessTokenValiditySeconds(authProperties.getAccessTokenValiditySeconds());
		tokenServices.setRefreshTokenValiditySeconds(authProperties.getRefreshTokenValiditySeconds());
		return tokenServices;
	}

}
