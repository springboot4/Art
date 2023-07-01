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

import com.art.common.security.core.handler.ArtAuthenticationFailureHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2ClientAuthenticationConfigurer;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/12 15:58
 */
public class OAuth2ClientAuthenticationConfigurerCustomizer
		implements Customizer<OAuth2ClientAuthenticationConfigurer> {

	/**
	 * @param oAuth2ClientAuthenticationConfigurer the input argument
	 */
	@Override
	public void customize(OAuth2ClientAuthenticationConfigurer oAuth2ClientAuthenticationConfigurer) {
		oAuth2ClientAuthenticationConfigurer
			// 登录失败处理
			.errorResponseHandler(new ArtAuthenticationFailureHandler());
	}

}
