package com.fxz.auth.configure;

import com.fxz.auth.extension.captcha.CaptchaTokenGranter;
import com.fxz.auth.properties.FxzAuthProperties;
import com.fxz.auth.service.FxzUserDetailServiceImpl;
import com.fxz.auth.translator.FxzWebResponseExceptionTranslator;
import com.fxz.common.core.constant.SecurityConstants;
import com.fxz.common.security.component.FxzTokenEnhancer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
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

	private final DataSource dataSource;

	private final RedisTemplate redisTemplate;

	private final FxzTokenEnhancer fxzTokenEnhancer;

	private final AuthenticationManager authenticationManager;

	private final RedisConnectionFactory redisConnectionFactory;

	private final FxzUserDetailServiceImpl userDetailService;

	private final FxzAuthProperties authProperties;

	private final FxzWebResponseExceptionTranslator fxzWebResponseExceptionTranslator;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		// 允许表单认证
		security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(fxzClientDetailsService());
	}

	@Override
	@SuppressWarnings("all")
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// 获取原有默认的授权者(授权码模式、密码模式、客户端模式、简化模式)
		List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

		// 添加验证码授权模式授权者
		granterList.add(new CaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory(), authenticationManager, redisTemplate));

		CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);

		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenStore(tokenStore())
				.tokenEnhancer(fxzTokenEnhancer).userDetailsService(userDetailService)
				.authenticationManager(authenticationManager).tokenServices(defaultTokenServices())
				.pathMapping("/oauth/confirm_access", "/token/confirm_access").tokenGranter(compositeTokenGranter)
				.exceptionTranslator(fxzWebResponseExceptionTranslator);
	}

	/**
	 * 客户端信息加载处理
	 * @return ClientDetailsService
	 */
	@Bean
	public ClientDetailsService fxzClientDetailsService() {
		JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
		clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
		return clientDetailsService;
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
		tokenServices.setTokenEnhancer(fxzTokenEnhancer);
		tokenServices.setAccessTokenValiditySeconds(authProperties.getAccessTokenValiditySeconds());
		tokenServices.setRefreshTokenValiditySeconds(authProperties.getRefreshTokenValiditySeconds());
		return tokenServices;
	}

}
