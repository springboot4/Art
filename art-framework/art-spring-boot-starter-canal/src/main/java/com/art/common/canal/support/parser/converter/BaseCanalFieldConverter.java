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

package com.art.common.canal.support.parser.converter;

import com.art.common.canal.util.StringUtils;

import java.sql.SQLType;

/**
 * @author fxz
 */
public abstract class BaseCanalFieldConverter<T> implements BinLogFieldConverter<String, T> {

	private final SQLType sqlType;

	private final Class<?> klass;

	protected BaseCanalFieldConverter(SQLType sqlType, Class<?> klass) {
		this.sqlType = sqlType;
		this.klass = klass;
	}

	@Override
	public T convert(String source) {
		if (StringUtils.X.isEmpty(source)) {
			return null;
		}
		return convertInternal(source);
	}

	/**
	 * 内部转换方法
	 * @param source 源字符串
	 * @return T
	 */
	protected abstract T convertInternal(String source);

	/**
	 * 返回SQL类型
	 * @return SQLType
	 */
	public SQLType sqlType() {
		return sqlType;
	}

	/**
	 * 返回类型
	 * @return Class<?>
	 */
	public Class<?> typeKlass() {
		return klass;
	}

}
