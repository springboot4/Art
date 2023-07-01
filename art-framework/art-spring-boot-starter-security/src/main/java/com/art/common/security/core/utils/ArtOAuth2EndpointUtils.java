/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.security.core.utils;

import cn.hutool.core.map.MapUtil;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author fxz
 */
@UtilityClass
public class ArtOAuth2EndpointUtils {

	public final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	public OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {

		OAuth2ClientAuthenticationToken clientPrincipal = null;

		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}

		if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}

		throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
	}

	public MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((key, values) -> {
			for (String value : values) {
				parameters.add(key, value);
			}
		});
		return parameters;
	}

	public void throwError(String errorCode, String parameterName, String errorUri) {
		OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
		throw new OAuth2AuthenticationException(error);
	}

	public void checkMustParameters(MultiValueMap<String, String> parameters, String... parameter) {
		for (String p : parameter) {
			String captcha = parameters.getFirst(p);
			if (!StringUtils.hasText(captcha) || parameters.get(p).size() != 1) {
				throwError(OAuth2ErrorCodes.INVALID_REQUEST, p, ACCESS_TOKEN_REQUEST_ERROR_URI);
			}
		}
	}

	public void checkOptionalParameters(MultiValueMap<String, String> parameters, String... parameter) {
		for (String p : parameter) {
			String val = parameters.getFirst(p);
			if (StringUtils.hasText(val) && parameters.get(p).size() != 1) {
				ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, p, ACCESS_TOKEN_REQUEST_ERROR_URI);
			}
		}
	}

	/**
	 * 格式化输出token 信息
	 * @param authentication 用户认证信息
	 * @param claims 扩展信息
	 * @return
	 */
	public OAuth2AccessTokenResponse sendAccessTokenResponse(OAuth2Authorization authentication,
			Map<String, Object> claims) {

		OAuth2AccessToken accessToken = authentication.getAccessToken().getToken();
		OAuth2RefreshToken refreshToken = authentication.getRefreshToken().getToken();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
			.tokenType(accessToken.getTokenType())
			.scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}

		if (MapUtil.isNotEmpty(claims)) {
			builder.additionalParameters(claims);
		}
		return builder.build();
	}

}
