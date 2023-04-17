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

package com.art.common.security.client.http;

import cn.hutool.core.text.StrPool;
import com.art.common.core.constant.ResultCode;
import com.art.common.core.model.Result;
import com.art.common.core.util.WebUtil;
import com.art.common.jackson.util.JacksonUtil;
import com.art.common.security.authentication.gitee.OAuth2GiteeAuthenticationToken;
import com.art.common.security.client.GiteeRegisteredClientRepository;
import com.art.common.security.client.core.constants.GiteeEndpointEnums;
import com.art.common.security.client.core.endpoint.OAuth2GiteeParameterNames;
import com.art.common.security.client.exception.StateGiteeException;
import com.art.common.security.client.properties.GiteeProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.art.common.security.client.core.constants.GiteeEndpointEnums.TOKEN_URL;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/15 15:48
 */
@Slf4j
@RequiredArgsConstructor
public class GiteeCodeHttpFilter implements Filter {

	private final GiteeRegisteredClientRepository giteeRegisteredClientRepository;

	public static final String PREFIX_URL = "/gitee/code";

	/**
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String requestUri = request.getRequestURI();
		String appid = requestUri.replace(PREFIX_URL + StrPool.SLASH, "");
		String code = request.getParameter(OAuth2ParameterNames.CODE);
		String state = request.getParameter(OAuth2ParameterNames.STATE);
		String binding = request.getParameter(OAuth2GiteeParameterNames.BINDING);

		boolean stateValid = giteeRegisteredClientRepository.stateValid(appid, code, state);
		if (!stateValid) {
			OAuth2Error oAuth2Error = new OAuth2Error(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), "非法状态码！",
					GiteeEndpointEnums.AUTHORIZE_URL);
			throw new StateGiteeException(oAuth2Error);
		}

		GiteeProperties.Gitee gitee = giteeRegisteredClientRepository.getGiteeByAppid(appid);
		Map<String, String> uriVariables = new HashMap<>(8);
		uriVariables.put(OAuth2ParameterNames.GRANT_TYPE, OAuth2GiteeAuthenticationToken.GITEE.getValue());
		uriVariables.put(OAuth2GiteeParameterNames.APPID, appid);
		uriVariables.put(OAuth2ParameterNames.CODE, code);
		uriVariables.put(OAuth2ParameterNames.STATE, state);
		uriVariables.put(OAuth2ParameterNames.CLIENT_ID, gitee.getClientId());
		uriVariables.put(OAuth2ParameterNames.CLIENT_SECRET, gitee.getClientSecret());
		uriVariables.put(OAuth2GiteeParameterNames.BINDING, binding);

		// 请求OAuth2内置的授权端点
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		messageConverters.add(5, new OAuth2AccessTokenResponseHttpMessageConverter());
		String res = restTemplate.postForObject(gitee.getTokenUrl() + TOKEN_URL, httpEntity, String.class,
				uriVariables);

		Map<String, Object> responseMap = JacksonUtil.parseObject(res, new TypeReference<Map<String, Object>>() {
		});

		String resCode = (String) responseMap.get("code");
		if (resCode == null) {
			DefaultMapOAuth2AccessTokenResponseConverter converter = new DefaultMapOAuth2AccessTokenResponseConverter();
			OAuth2AccessTokenResponse oauth2AccessTokenResponse = converter.convert(responseMap);

			OAuth2AccessToken oauth2AccessToken = oauth2AccessTokenResponse.getAccessToken();
			OAuth2RefreshToken oauth2RefreshToken = oauth2AccessTokenResponse.getRefreshToken();

			String tenantId = String.valueOf(oauth2AccessTokenResponse.getAdditionalParameters().get("tenantId"));

			String successUrl = gitee.getSuccessRedirectUrl();
			String accessToken = oauth2AccessToken.getTokenValue();
			Instant expiresAt = oauth2AccessToken.getExpiresAt();
			String refreshToken = "";
			Instant refreshTokenExpiresAt = null;
			if (oauth2RefreshToken != null) {
				refreshToken = oauth2RefreshToken.getTokenValue();
				refreshTokenExpiresAt = oauth2RefreshToken.getExpiresAt();
			}

			response.sendRedirect(String.format(
					"%s?store=true&accessToken=%s&refreshToken=%s&tenantId=%s&expiresAt=%s&refreshTokenExpiresAt=%s",
					successUrl, accessToken, refreshToken, tenantId, expiresAt, refreshTokenExpiresAt));
		}
		else {
			// 存在响应代码时，代表程序报错，直接响应错误内容
			WebUtil.makeFailureResponse(response, Result.failed());
		}
	}

}
