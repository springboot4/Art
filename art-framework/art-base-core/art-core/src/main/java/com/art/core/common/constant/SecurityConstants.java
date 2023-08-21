
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

package com.art.core.common.constant;

/**
 * @author fxz
 */
public final class SecurityConstants {

	/**
	 * 内部
	 */
	public static final String FROM_IN = "Y";

	/**
	 * 标志
	 */
	public static final String FROM = "from";

	/**
	 * 请求header
	 */
	public static final String HEADER_INNER = FROM + "=" + FROM_IN;

	/**
	 * 短信验证码key前缀
	 */
	public static final String SMS_CODE_PREFIX = "SMS_CODE:";

	/**
	 * 系统管理 web 客户端ID
	 */
	public static final String ADMIN_CLIENT_ID = "fxz";

	/**
	 * 客户端编号
	 */
	public static final String CLIENT_ID = "client_id";

	/**
	 * Basic认证前缀
	 */
	public static final String BASIC_PREFIX = "Basic ";

	public static final String MOBILE = "mobile";

	public static final String CAPTCHA = "captcha";

}
