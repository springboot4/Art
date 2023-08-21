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

package com.art.common.security.client.core.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/15 15:24
 * @see OAuth2ParameterNames 在 OAuth 参数注册表中定义并由授权端点、令牌端点和令牌撤销端点使用的标准和自定义（非标准）参数名称。
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuth2GiteeParameterNames {

	/**
	 * 码云应用id
	 */
	public static final String APPID = "appid";

	/**
	 * 是否需要绑定
	 */
	public static final String BINDING = "binding";

}
