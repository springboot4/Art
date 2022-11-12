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

package com.fxz.auth.properties;

import lombok.Data;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 16:45
 */
@Data
public class FxzValidateCodeProperties {

	/**
	 * 验证码有效时间，单位秒
	 */
	private Long time = 12000L;

	/**
	 * 验证码类型，可选值 png和 gif
	 */
	private String type = "png";

	/**
	 * 图片宽度，px
	 */
	private Integer width = 130;

	/**
	 * 图片高度，px
	 */
	private Integer height = 48;

	/**
	 * 验证码位数
	 */
	private Integer length = 4;

	/**
	 * 验证码值的类型 1. 数字加字母 2. 纯数字 3. 纯字母
	 */
	private Integer charType = 2;

}
