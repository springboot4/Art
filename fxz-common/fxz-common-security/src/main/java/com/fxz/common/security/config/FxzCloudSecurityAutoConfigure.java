package com.fxz.common.security.config;

import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.security.FxzUserInfoTokenServices;
import com.fxz.common.security.handler.FxzAccessDeniedHandler;
import com.fxz.common.security.handler.FxzAuthExceptionEntryPoint;
import com.fxz.common.security.permission.PermissionService;
import com.fxz.common.security.properties.FxzCloudSecurityProperties;
import com.fxz.common.security.util.SecurityUtil;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.util.Base64Utils;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-06 18:15
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(FxzCloudSecurityProperties.class)
public class FxzCloudSecurityAutoConfigure {

	/**
	 * 接口权限判断工具
	 */
	@Bean("pms")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	/**
	 * 用于处理403类型异常
	 */
	@Bean
	@ConditionalOnMissingBean(name = "accessDeniedHandler")
	public FxzAccessDeniedHandler accessDeniedHandler() {
		return new FxzAccessDeniedHandler();
	}

	/**
	 * 用于处理401类型异常
	 */
	@Bean
	@ConditionalOnMissingBean(name = "authenticationEntryPoint")
	public FxzAuthExceptionEntryPoint authenticationEntryPoint() {
		return new FxzAuthExceptionEntryPoint();
	}

	/**
	 * 注入密码编码器
	 */
	@Bean
	@ConditionalOnMissingBean(value = PasswordEncoder.class)
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 配置拦截器,所有请求必须通过网关
	 */
	@Bean
	public FxzServerProtectConfigure fxzServerProtectInterceptor() {
		return new FxzServerProtectConfigure();
	}

	@Bean
	@Primary
	@ConditionalOnMissingBean(DefaultTokenServices.class)
	public FxzUserInfoTokenServices fxzUserInfoTokenServices(ResourceServerProperties properties) {
		return new FxzUserInfoTokenServices(properties.getUserInfoUri(), properties.getClientId());
	}

	/**
	 * 为feign请求头添加令牌
	 */
	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor() {
		return requestTemplate -> {
			String gatewayToken = new String(Base64Utils.encode(FxzConstant.GATEWAY_TOKEN_VALUE.getBytes()));
			requestTemplate.header(FxzConstant.GATEWAY_TOKEN_HEADER, gatewayToken);
			String authorizationToken = SecurityUtil.getCurrentTokenValue();
			if (StringUtils.isNotBlank(authorizationToken)) {
				requestTemplate.header(HttpHeaders.AUTHORIZATION, FxzConstant.OAUTH2_TOKEN_TYPE + authorizationToken);
			}
		};
	}

}
