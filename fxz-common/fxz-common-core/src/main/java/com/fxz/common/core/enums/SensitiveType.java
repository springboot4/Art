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

package com.fxz.common.core.enums;

/**
 * 敏感信息
 *
 * @author fxz
 * @date 20224/25
 */
public enum SensitiveType {

	/**
	 * 中文名
	 */
	CHINESE_NAME,
	/**
	 * 用户id
	 */
	USER_ID,
	/**
	 * 密码
	 */
	PASSWORD,
	/**
	 * 身份证号
	 */
	ID_CARD,
	/**
	 * 座机号
	 */
	FIXED_PHONE,
	/**
	 * 手机号
	 */
	MOBILE_PHONE,
	/**
	 * ip地址
	 */
	IP,
	/**
	 * 地址
	 */
	ADDRESS,
	/**
	 * 电子邮件
	 */
	EMAIL,
	/**
	 * 车牌号
	 */
	CAR_LICENSE,
	/**
	 * 银行卡
	 */
	BANK_CARD,
	/**
	 * 公司开户银行联号
	 */
	CNAPS_CODE,

	/**
	 * 其他
	 */
	OTHER;

}
