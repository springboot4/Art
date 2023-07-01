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

package com.art.common.security.authorization;

import com.art.common.security.authentication.gitee.OAuth2GiteeAuthenticationProvider;
import com.art.common.security.authentication.password.DaoAuthenticationProvider;
import com.art.common.security.authentication.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.art.common.security.authentication.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Arrays;
import java.util.List;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/12 15:29
 */
public class AuthenticationProvidersCustomCustomizer implements Customizer<HttpSecurity> {

	/**
	 * @param httpSecurity the input argument
	 */
	@Override
	public void customize(HttpSecurity httpSecurity) {
		List<AuthenticationProvider> providerList = Arrays.asList(
				// 支持OAuth2GiteeAuthenticationToken类型的认证
				new OAuth2GiteeAuthenticationProvider(httpSecurity),

				// 支持OAuth2ResourceOwnerSmsAuthenticationToken类型的认证
				new OAuth2ResourceOwnerSmsAuthenticationProvider(httpSecurity),

				// 支持OAuth2ResourceOwnerPasswordAuthenticationToken类型的认证
				new OAuth2ResourceOwnerPasswordAuthenticationProvider(httpSecurity),

				// 支持UsernamePasswordAuthenticationToken类型的认证
				new DaoAuthenticationProvider());

		providerList.forEach(httpSecurity::authenticationProvider);
	}

}
