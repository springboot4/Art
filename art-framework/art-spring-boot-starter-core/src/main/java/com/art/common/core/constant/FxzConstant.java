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

package com.art.common.core.constant;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 13:10
 */
public interface FxzConstant {

	/**
	 * GATEWAY请求头TOKEN名称（不要有空格）
	 */
	String GATEWAY_TOKEN_HEADER = "GatewayToken";

	/**
	 * GATEWAY请求头TOKEN值
	 */
	String GATEWAY_TOKEN_VALUE = "fxz:gateway:$**$";

	/**
	 * gif类型
	 */
	String GIF = "gif";

	/**
	 * png类型
	 */
	String PNG = "png";

	/**
	 * UTF-8 字符集
	 */
	String UTF8 = "UTF-8";

	/**
	 * GBK 字符集
	 */
	String GBK = "GBK";

	/**
	 * RMI 远程方法调用
	 */
	String LOOKUP_RMI = "rmi:";

	/**
	 * LDAP 远程方法调用
	 */
	String LOOKUP_LDAP = "ldap:";

	/**
	 * LDAPS 远程方法调用
	 */
	String LOOKUP_LDAPS = "ldaps:";

	/**
	 * http请求
	 */
	String HTTP = "http://";

	/**
	 * https请求
	 */
	String HTTPS = "https://";

	/**
	 * 验证码 key前缀
	 */
	String CODE_PREFIX = "fxz.captcha.";

	String OAUTH2_TOKEN_TYPE = "Bearer ";

	String OAUTH2_TOKEN_TYPE_LOW = "bearer";

	Integer STATUS_YES = 1;

}
