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
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 16:18
 */
@Getter
@AllArgsConstructor
public enum GlobalStatusEnum {

	/**
	 * 开启状态
	 */
	ENABLE(0, "开启"),

	/**
	 * 关闭状态
	 */
	DISABLE(1, "关闭");

	/**
	 * 状态值
	 */
	private final Integer value;

	/**
	 * 状态描述
	 */
	private final String label;

}
