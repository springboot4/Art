package com.fxz.auth.configure;

import com.fxz.auth.extension.captcha.CaptchaTokenGranter;
import com.fxz.auth.properties.FxzAuthProperties;
import com.fxz.auth.properties.FxzClientsProperties;
import com.fxz.auth.service.FxzUserDetailServiceImpl;
import com.fxz.auth.translator.FxzWebResponseExceptionTranslator;
import com.fxz.common.core.constant.SecurityConstants;
import com.fxz.common.core.service.RedisService;
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
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @Description 认证服务器配置
 * @date 2021-11-27 16:10
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class FxzAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

	private final RedisService redisService;

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
		// todo ClientDetailsService
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
		// 获取原有默认的授权者(授权码模式、密码模式、客户端模式、简化模式)
		List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

		// 添加验证码授权模式授权者
		granterList.add(new CaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory(), authenticationManager, redisService));

		CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);

		endpoints.tokenStore(tokenStore()).userDetailsService(userDetailService)
				.authenticationManager(authenticationManager).tokenServices(defaultTokenServices())
				.tokenGranter(compositeTokenGranter).exceptionTranslator(fxzWebResponseExceptionTranslator);
	}

	@Bean
	public TokenStore tokenStore() {
		RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
		redisTokenStore.setPrefix(SecurityConstants.TOKEN_PREFIX);
		return redisTokenStore;
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
