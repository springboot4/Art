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

package com.art.common.security.authentication.gitee;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/15 16:09
 */
public class OAuth2GiteeAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	public static final AuthorizationGrantType GITEE = new AuthorizationGrantType("gitee");

	@Getter
	private final String appid;

	@Getter
	private final String code;

	/**
	 * @see OAuth2ParameterNames#SCOPE
	 */
	@Getter
	private final String scope;

	@Getter
	private final String state;

	@Getter
	private final String binding;

	public OAuth2GiteeAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters,
			String appid, String code, String scope, String state, String binding) {
		super(OAuth2GiteeAuthenticationToken.GITEE, clientPrincipal, additionalParameters);
		Assert.hasText(appid, "appid 不能为空");
		Assert.hasText(code, "code 不能为空");
		this.appid = appid;
		this.code = code;
		this.scope = scope;
		this.state = state;
		this.binding = binding;
	}

}
