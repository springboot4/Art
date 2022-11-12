/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.fxz.common.security.constant;

/**
 * 端点常量
 *
 * @author fxz
 */
public interface EndpointConstant {

	String ALL = "/**";

	String OAUTH_ALL = "/oauth/**";

	String OAUTH_AUTHORIZE = "/oauth/authorize";

	String OAUTH_CHECK_TOKEN = "/oauth/check_token";

	String OAUTH_CONFIRM_ACCESS = "/oauth/confirm_access";

	String OAUTH_TOKEN = "/oauth/token";

	String OAUTH_TOKEN_KEY = "/oauth/token_key";

	String OAUTH_ERROR = "/oauth/error";

	String ACTUATOR_ALL = "/actuator/**";

	String LOGIN = "/login";

}
