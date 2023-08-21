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

package com.art.core.common.support.base;

import java.util.EnumSet;
import java.util.Objects;

/**
 * 枚举通用接口
 *
 * @author fxz
 */
@SuppressWarnings("all")
public interface IBaseEnum<T> {

	/**
	 * 获取枚举值
	 * @return 枚举值
	 */
	T getValue();

	/**
	 * 获取枚举标签
	 * @return 枚举标签
	 */
	String getLabel();

	/**
	 * 根据枚举值和枚举类获取枚举
	 * @param value 枚举值
	 * @param clazz 枚举类型
	 * @return 枚举
	 */
	static <E extends Enum<E> & IBaseEnum> E getEnumByValue(Object value, Class<E> clazz) {
		Objects.requireNonNull(value);

		// 获取类型下的所有枚举
		EnumSet<E> allEnums = EnumSet.allOf(clazz);
		return allEnums.stream().filter(e -> value.equals(e.getValue())).findFirst().orElse(null);
	}

	/**
	 * 根据枚举值和枚举类获取枚举标签
	 * @param value 枚举值
	 * @param clazz 枚举类型
	 * @return 枚举标签
	 */
	static <E extends Enum<E> & IBaseEnum> String getLabelByValue(Object value, Class<E> clazz) {
		E matchEnum = getEnumByValue(value, clazz);

		String label = null;
		if (Objects.nonNull(matchEnum)) {
			label = matchEnum.getLabel();
		}

		return label;
	}

	/**
	 * 根据枚举标签和枚举类获取枚举
	 * @param label 枚举标签
	 * @param clazz 枚举类型
	 * @return 枚举
	 */
	static <E extends Enum<E> & IBaseEnum> E getEnumByLabel(String label, Class<E> clazz) {
		Objects.requireNonNull(label);

		// 获取类型下的所有枚举
		EnumSet<E> allEnums = EnumSet.allOf(clazz);

		return allEnums.stream().filter(e -> label.equals(e.getLabel())).findFirst().orElse(null);
	}

	/**
	 * 根据枚举标签和枚举类获取枚举值
	 * @param label 枚举标签
	 * @param clazz 枚举类型
	 * @return 枚举值
	 */
	static <E extends Enum<E> & IBaseEnum> Object getValueByLabel(String label, Class<E> clazz) {
		E matchEnum = getEnumByLabel(label, clazz);

		Object value = null;
		if (Objects.nonNull(matchEnum)) {
			value = matchEnum.getValue();
		}

		return value;
	}

}
