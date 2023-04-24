/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.security.core.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.art.common.core.util.AsyncUtil;
import com.art.common.core.util.SpringUtil;
import com.art.system.api.log.LogServiceApi;
import com.art.system.api.log.dto.OperLogDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/12 15:17
 */
@RequiredArgsConstructor
public class ArtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	/**
	 * 认证成功事件处理
	 * @param request the request which caused the successful authentication
	 * @param response the response
	 * @param authentication the <tt>Authentication</tt> object which was created during
	 * the authentication process.
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		AsyncUtil.parallel(new AuthenticationSuccessLogFun(authentication, request),
				new AuthenticationSuccessResponseFun(authentication, response));
	}

	@RequiredArgsConstructor
	private static class AuthenticationSuccessLogFun implements Runnable {

		private final Authentication authentication;

		private final HttpServletRequest request;

		@Override
		public void run() {
			OperLogDTO logDto = new OperLogDTO();

			logDto.setTitle("用户登录");
			logDto.setUpdateTime(LocalDateTime.now());
			logDto.setStatus(0);
			logDto.setOperName(authentication.getName());
			logDto.setCreateBy(authentication.getName());
			logDto.setOperIp(JakartaServletUtil.getClientIP(request));
			logDto.setOperParam(authentication.getName());
			logDto.setBusinessType(4);
			logDto.setRequestMethod(request.getMethod());
			LogServiceApi logServiceApi = SpringUtil.getBean(LogServiceApi.class);
			logServiceApi.add(logDto);
		}

	}

	@RequiredArgsConstructor
	private static class AuthenticationSuccessResponseFun implements Runnable {

		private final Authentication authentication;

		private final HttpServletResponse response;

		@SneakyThrows
		@Override
		public void run() {
			HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
			OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

			OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
			OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
			Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

			OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
				.tokenType(accessToken.getTokenType())
				.scopes(accessToken.getScopes());

			if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
				builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
			}
			if (refreshToken != null) {
				builder.refreshToken(refreshToken.getTokenValue());
			}
			if (!CollectionUtils.isEmpty(additionalParameters)) {
				builder.additionalParameters(additionalParameters);
			}
			OAuth2AccessTokenResponse accessTokenResponse = builder.build();

			ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

			SecurityContextHolder.clearContext();

			accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
		}

	}

}
