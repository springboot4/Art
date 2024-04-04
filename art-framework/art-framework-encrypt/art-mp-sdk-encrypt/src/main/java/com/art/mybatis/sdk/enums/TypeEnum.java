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

package com.art.mybatis.sdk.enums;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 对象类型枚举
 *
 * @author fxz
 */
public enum TypeEnum {

	/**
	 * 基础类型或其包装类
	 */
	PRIMITIVE_OR_WRAPPER,

	/**
	 * 字符串
	 */
	STRING,

	/**
	 * Map
	 */
	MAP,

	/**
	 * 集合
	 */
	COLLECTION,

	/**
	 * 数组
	 */
	ARRAY,

	/**
	 * jdk自带的java类
	 */
	SYSTEM_BEAN,

	/**
	 * 我们定义的java类
	 */
	CUSTOM_BEAN;

	/**
	 * 解析clazz的数据类型
	 * @param clazz 待解析的类
	 * @return clazz的类型
	 */
	public static TypeEnum parseType(Class<?> clazz) {
		Objects.requireNonNull(clazz, "clazz参数不能为null!");

		// 八大基本类型和Void
		if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
			return PRIMITIVE_OR_WRAPPER;
		}

		// String类型
		if (TypeUtils.isAssignable(clazz, String.class)) {
			return STRING;
		}

		// Map类型
		if (TypeUtils.isAssignable(clazz, Map.class)) {
			return MAP;
		}

		// Collection类型
		if (TypeUtils.isAssignable(clazz, Collection.class)) {
			return COLLECTION;
		}

		// 数组类型
		if (TypeUtils.isArrayType(clazz)) {
			return ARRAY;
		}

		// jdk内置类型
		if (clazz.getName().startsWith("java")) {
			return SYSTEM_BEAN;
		}

		// 自定义java类型
		return CUSTOM_BEAN;
	}

}
