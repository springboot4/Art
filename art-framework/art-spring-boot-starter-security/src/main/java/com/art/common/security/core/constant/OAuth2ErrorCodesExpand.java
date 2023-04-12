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

package com.art.common.security.core.constant;

public interface OAuth2ErrorCodesExpand {

	/**
	 * 用户名未找到
	 */
	String USERNAME_NOT_FOUND = "username_not_found";

	/**
	 * 错误凭证
	 */
	String BAD_CREDENTIALS = "bad_credentials";

	/**
	 * 用户被锁
	 */
	String USER_LOCKED = "user_locked";

	/**
	 * 用户禁用
	 */
	String USER_DISABLE = "user_disable";

	/**
	 * 用户过期
	 */
	String USER_EXPIRED = "user_expired";

	/**
	 * 证书过期
	 */
	String CREDENTIALS_EXPIRED = "credentials_expired";

	/**
	 * scope 为空异常
	 */
	String SCOPE_IS_EMPTY = "scope_is_empty";

	/**
	 * 令牌不存在
	 */
	String TOKEN_MISSING = "token_missing";

	/**
	 * 未知的登录异常
	 */
	String UN_KNOW_LOGIN_ERROR = "un_know_login_error";

	/**
	 * 不合法的Token
	 */
	String INVALID_BEARER_TOKEN = "invalid_bearer_token";

}
