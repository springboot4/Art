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

package com.fxz.common.dataPermission.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据范围枚举类
 *
 * 用于实现数据级别的权限
 *
 * @author fxz
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

	/**
	 * 全部数据权限
	 */
	ALL(1),

	/**
	 * 指定部门数据权限
	 */
	DEPT_CUSTOM(2),

	/**
	 * 部门数据权限
	 */
	DEPT_ONLY(3),

	/**
	 * 部门及以下数据权限
	 */
	DEPT_AND_CHILD(4),

	/**
	 * 仅本人数据权限
	 */
	SELF(5);

	/**
	 * 范围
	 */
	private final Integer scope;

}
