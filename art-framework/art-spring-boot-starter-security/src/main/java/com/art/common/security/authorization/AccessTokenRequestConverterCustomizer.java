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

package com.art.common.security.authorization;

import com.art.common.security.authentication.gitee.OAuth2GiteeAuthenticationConverter;
import com.art.common.security.authentication.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.art.common.security.authentication.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/12 15:34
 */
public interface AccessTokenRequestConverterCustomizer {

	List<AuthenticationConverter> CONVERTERS = Arrays.asList(
			// 自定义的gitee oauth2登录
			new OAuth2GiteeAuthenticationConverter(),

			// 自定义的验证码模式Converter
			new OAuth2ResourceOwnerSmsAuthenticationConverter(),

			// 自定义的密码模式Converter
			new OAuth2ResourceOwnerPasswordAuthenticationConverter(),

			// 内置的刷新tokenConverter
			new OAuth2RefreshTokenAuthenticationConverter(),

			// 内置的客户端凭据Converter
			new OAuth2ClientCredentialsAuthenticationConverter(),

			// 内置的授权码Converter
			new OAuth2AuthorizationCodeAuthenticationConverter(),

			// 内置的授权码请求Converter
			new OAuth2AuthorizationCodeRequestAuthenticationConverter());

	static AuthenticationConverter customizer() {
		return new DelegatingAuthenticationConverter(CONVERTERS);
	}

}
