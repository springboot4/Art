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

package com.art.common.security.authentication.sms;

import com.art.common.core.constant.SecurityConstants;
import com.art.common.security.core.utils.ArtOAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 短信认证请求转换
 *
 * @author fxz
 */
public class OAuth2ResourceOwnerSmsAuthenticationConverter implements AuthenticationConverter {

	public static final AuthorizationGrantType SMS = new AuthorizationGrantType("sms_code");

	/**
	 * 支持短信认证
	 * @param grantType 授权类型
	 */
	public boolean support(String grantType) {
		return OAuth2ResourceOwnerSmsAuthenticationConverter.SMS.getValue().equals(grantType);
	}

	/**
	 * POST /oauth2/token?mobile=19806082431&captcha=1212&grant_type=sms_code&scope=server
	 * <p/>
	 * 用于从HttpServlet请求转换为特定类型的身份验证的策略。用于使用适当的AuthenticationManager进行身份验证。
	 * 如果结果为null，则表示不应进行身份验证尝试。如果存在无效的Authentication方案值，也可以在转换（HttpServlet请求）中抛出AuthenticationException。
	 * @param request 认证请求
	 * @return Authentication
	 */
	@Override
	public Authentication convert(HttpServletRequest request) {
		// 获取grant_type
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!support(grantType)) {
			return null;
		}

		// 获取请求参数
		MultiValueMap<String, String> parameters = ArtOAuth2EndpointUtils.getParameters(request);
		// 检验必须参数：手机号、验证码
		ArtOAuth2EndpointUtils.checkMustParameters(parameters, SecurityConstants.MOBILE, SecurityConstants.CAPTCHA);
		// 检验可选参数: scope
		ArtOAuth2EndpointUtils.checkOptionalParameters(parameters, OAuth2ParameterNames.SCOPE);

		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		// 获取当前已经认证的客户端信息
		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		if (clientPrincipal == null) {
			ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT,
					ArtOAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 扩展信息
		Map<String, Object> additionalParameters = parameters.entrySet()
			.stream()
			.filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
					&& !e.getKey().equals(OAuth2ParameterNames.SCOPE))
			.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

		// 创建token
		return new OAuth2ResourceOwnerSmsAuthenticationToken(OAuth2ResourceOwnerSmsAuthenticationConverter.SMS,
				clientPrincipal, requestedScopes, additionalParameters);
	}

}