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

package com.art.common.security.authentication.gitee;

import com.art.common.security.client.core.endpoint.OAuth2GiteeParameterNames;
import com.art.common.security.core.utils.ArtOAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/15 16:31
 */
public class OAuth2GiteeAuthenticationConverter implements AuthenticationConverter {

	/**
	 * @param grantType 授权类型
	 */
	public boolean support(String grantType) {
		return OAuth2GiteeAuthenticationToken.GITEE.getValue().equals(grantType);
	}

	/**
	 * @param request
	 * @return
	 */
	@Override
	public Authentication convert(HttpServletRequest request) {
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!support(grantType)) {
			return null;
		}

		// 获取已经认证的系统内客户端信息
		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

		MultiValueMap<String, String> parameters = ArtOAuth2EndpointUtils.getParameters(request);
		// 检验必须的参数
		ArtOAuth2EndpointUtils.checkMustParameters(parameters, OAuth2ParameterNames.CODE,
				OAuth2GiteeParameterNames.APPID);

		// 授权码
		String code = parameters.getFirst(OAuth2ParameterNames.CODE);
		// 码云应用id
		String appid = parameters.getFirst(OAuth2GiteeParameterNames.APPID);
		// 请求的权限
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		// 状态码
		String state = parameters.getFirst(OAuth2ParameterNames.STATE);
		// 是否绑定用户
		String binding = parameters.getFirst(OAuth2GiteeParameterNames.BINDING);

		Map<String, Object> additionalParameters = new HashMap<>(4);
		parameters.forEach((key, value) -> {
			if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.CLIENT_ID)
					&& !key.equals(OAuth2ParameterNames.CLIENT_SECRET) && !key.equals(OAuth2ParameterNames.CODE)
					&& !key.equals(OAuth2ParameterNames.REDIRECT_URI) && !key.equals(OAuth2GiteeParameterNames.APPID)
					&& !key.equals(OAuth2ParameterNames.SCOPE)) {
				additionalParameters.put(key, value.get(0));
			}
		});

		return new OAuth2GiteeAuthenticationToken(clientPrincipal, additionalParameters, appid, code, scope, state,
				binding);
	}

}
