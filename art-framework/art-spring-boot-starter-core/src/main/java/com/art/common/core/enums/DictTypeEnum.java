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

package com.art.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fxz
 */

@Getter
@AllArgsConstructor
public enum DictTypeEnum {

	/**
	 * 字典类型-业务类型
	 */
	BIZ("0", "业务类"),

	/**
	 * 字典类型-系统内置（不可修改）
	 */
	SYSTEM("1", "系统内置");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
