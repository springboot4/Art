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

package com.art.common.canal.support;

import com.art.common.canal.util.AssertUtils;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author fxz
 */
public abstract class BaseParameterizedTypeReferenceSupport<T> {

	@Getter
	private final Class<T> klass;

	@Getter
	private final Class<?> childKlass;

	@SuppressWarnings("unchecked")
	public BaseParameterizedTypeReferenceSupport() {
		childKlass = getClass();
		Type type = findParameterizedTypeReferenceSubClass(childKlass);
		AssertUtils.X.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		AssertUtils.X.isTrue(actualTypeArguments.length == 1, "Number of type arguments must be 1");
		this.klass = (Class<T>) actualTypeArguments[0];
	}

	/**
	 * 递归搜索ParameterizedType类型引用的父类 - 目的是获取泛型参数类型
	 * @param child child
	 * @return Class
	 */
	private static Type findParameterizedTypeReferenceSubClass(Class<?> child) {
		Type genericSuperclass = child.getGenericSuperclass();
		if (!(genericSuperclass instanceof ParameterizedType)) {
			Class<?> parent = child.getSuperclass();
			if (Object.class == parent) {
				throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
			}
			return findParameterizedTypeReferenceSubClass(parent);
		}
		return genericSuperclass;
	}

}
