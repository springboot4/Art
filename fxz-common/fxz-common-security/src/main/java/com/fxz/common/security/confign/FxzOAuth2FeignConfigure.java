package com.fxz.common.security.confign;

import com.fxz.common.core.entity.FxzConstant;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.Base64Utils;

/**
 * 为feign请求头添加令牌
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 12:44
 */
@SuppressWarnings("all")
public class FxzOAuth2FeignConfigure {

	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor() {
		return requestTemplate -> {
			// 添加 Gateway Token
			String gatewayToken = new String(Base64Utils.encode(FxzConstant.GATEWAY_TOKEN_VALUE.getBytes()));
			requestTemplate.header(FxzConstant.GATEWAY_TOKEN_HEADER, gatewayToken);

			Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
			if (details instanceof OAuth2AuthenticationDetails) {
				String authorizationToken = ((OAuth2AuthenticationDetails) details).getTokenValue();
				requestTemplate.header(HttpHeaders.AUTHORIZATION, "bearer " + authorizationToken);
			}
		};
	}

}
