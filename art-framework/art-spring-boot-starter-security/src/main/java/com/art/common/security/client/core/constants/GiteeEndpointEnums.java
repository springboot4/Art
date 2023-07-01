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

package com.art.common.security.client.core.constants;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/16 12:58
 */
public interface GiteeEndpointEnums {

	String ACCESS_TOKEN_URL = "https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}";

	String USERINFO_URL = "https://gitee.com/api/v5/user";

	/**
	 * gitee获取授权码端点url
	 */
	String AUTHORIZE_URL = "https://gitee.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s";

	/**
	 * 我们自己的授权服务器url
	 */
	String TOKEN_URL = "/oauth2/token?grant_type={grant_type}&appid={appid}&code={code}&state={state}&client_id={client_id}&client_secret={client_secret}&binding={binding}";

}
