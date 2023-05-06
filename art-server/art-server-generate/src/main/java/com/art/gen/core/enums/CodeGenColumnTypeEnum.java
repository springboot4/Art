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

package com.art.gen.core.enums;

import com.art.common.core.exception.FxzException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-03 16:14
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum CodeGenColumnTypeEnum {

	/**
	 * 数据库字段与java、ts数据类型的映射
	 */
	TINYINT("tinyint", "Integer", "number"), SMALLINT("smallint", "Integer", "number"),
	MEDIUMINT("mediumint", "Integer", "number"), INT("int", "Integer", "number"),
	INTEGER("integer", "Integer", "number"), BIGINT("bigint", "Long", "string"), FLOAT("float", "Float", "number"),
	DOUBLE("double", "Double", "number"), DECIMAL("decimal", "BigDecimal", "number"), BIT("bit", "Boolean", "boolean"),
	CHAR("char", "String", "string"), VARCHAR("varchar", "String", "string"),
	VARBINARY("VARBINARY", "byte[]", "unknown"), TINYTEXT("tinytext", "String", "string"),
	TEXT("text", "String", "string"), MEDIUMTEXT("mediumtext", "String", "string"),
	LONGTEXT("longtext", "String", "string"), DATE("date", "LocalDate", "string"),
	DATETIME("datetime", "LocalDateTime", "string"), TIME("time", "LocalTime", "string"),
	TIMESTAMP("timestamp", "LocalDateTime", "string"), JSON("json", "String", "string");

	private final String columnType;

	private final String javaType;

	private final String tsType;

	/**
	 * 数据库类型转换成java类型
	 */
	@SneakyThrows
	public static String convertJavaType(String columnType) {
		return Arrays.stream(CodeGenColumnTypeEnum.values())
			.filter(e -> Objects.equals(columnType, e.getColumnType()))
			.findFirst()
			.orElseThrow(() -> new FxzException(String.format("不支持的数据库字段类型:%s", columnType)))
			.getJavaType();
	}

	/**
	 * 数据库类型转换成ts类型
	 */
	public static String convertTsType(String columnType) {
		return Arrays.stream(CodeGenColumnTypeEnum.values())
			.filter(e -> Objects.equals(columnType, e.getColumnType()))
			.findFirst()
			.orElseThrow(() -> new FxzException("不支持的数据库字段类型"))
			.getTsType();
	}

}
