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

package com.fxz.common.canal.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fxz
 */
public enum ReflectionUtils {

	/**
	 * 单例
	 */
	X;

	private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

	private static final Map<Class<?>, Field[]> DECLARED_FIELDS_CACHE = new ConcurrentHashMap<>(32);

	private Field[] getDeclaredFields(Class<?> clazz) {
		AssertUtils.X.notNull(clazz, "Class must not be null");
		Field[] result = DECLARED_FIELDS_CACHE.get(clazz);
		if (Objects.isNull(result)) {
			try {
				result = clazz.getDeclaredFields();
				DECLARED_FIELDS_CACHE.put(clazz, result.length == 0 ? EMPTY_FIELD_ARRAY : result);
			}
			catch (Exception e) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName()
						+ "] from ClassLoader [" + clazz.getClassLoader() + "]", e);
			}
		}
		return result;
	}

	public void doWithFields(Class<?> clazz, FieldCallback fieldCallback) {
		doWithFields(clazz, fieldCallback, null);
	}

	public void doWithFields(Class<?> clazz, FieldCallback fieldCallback, FieldFilter fieldFilter) {
		Class<?> targetClass = clazz;
		do {
			Field[] fields = getDeclaredFields(targetClass);
			int len = fields.length;
			for (int index = 0; index < len; ++index) {
				Field field = fields[index];
				if (null == fieldFilter || fieldFilter.matches(field)) {
					try {
						fieldCallback.doWith(field);
					}
					catch (IllegalAccessException var10) {
						throw new IllegalStateException(
								"Not allowed to access field '" + field.getName() + "': " + var10);
					}
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);
	}

	@SuppressWarnings("all")
	public void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	@FunctionalInterface
	public interface FieldCallback {

		void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;

	}

	@FunctionalInterface
	public interface FieldFilter {

		boolean matches(Field field);

	}

}
