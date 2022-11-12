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

package com.fxz.common.canal.annotation;

import com.fxz.common.canal.support.parser.converter.BaseCanalFieldConverter;
import com.fxz.common.canal.support.parser.converter.NullCanalFieldConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.JDBCType;

/**
 * @author fxz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CanalField {

	/**
	 * 行名称
	 * @return columnName
	 */
	String columnName() default "";

	/**
	 * sql字段类型
	 * @return JDBCType
	 */
	JDBCType sqlType() default JDBCType.NULL;

	/**
	 * 字段转换器
	 * @return klass
	 */
	Class<? extends BaseCanalFieldConverter<?>> converterKlass() default NullCanalFieldConverter.class;

}
