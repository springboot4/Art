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

package com.art.common.security.core.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuth2ErrorCodesExpand {

	/**
	 * 用户名未找到
	 */
	public static final String USERNAME_NOT_FOUND = "username_not_found";

	/**
	 * 错误凭证
	 */
	public static final String BAD_CREDENTIALS = "bad_credentials";

	/**
	 * 用户被锁
	 */
	public static final String USER_LOCKED = "user_locked";

	/**
	 * 用户禁用
	 */
	public static final String USER_DISABLE = "user_disable";

	/**
	 * 用户过期
	 */
	public static final String USER_EXPIRED = "user_expired";

	/**
	 * 证书过期
	 */
	public static final String CREDENTIALS_EXPIRED = "credentials_expired";

}
