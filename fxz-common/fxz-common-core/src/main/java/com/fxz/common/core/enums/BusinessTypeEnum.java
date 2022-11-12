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

import lombok.Getter;

/**
 * 业务类型枚举
 *
 * @author fxz
 */

public enum BusinessTypeEnum implements IBaseEnum<Integer> {

	/**
	 * 用户业务
	 */
	USER(100, "用户"),
	/**
	 * 会员业务
	 */
	MEMBER(200, "会员"),
	/**
	 * 订单业务
	 */
	ORDER(300, "订单");

	@Getter
	private final Integer value;

	@Getter
	private final String label;

	BusinessTypeEnum(Integer value, String label) {
		this.value = value;
		this.label = label;
	}

}