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
	 * 数据库字段与java数据类型的映射
	 */
	TINYINT("tinyint", "Integer"), SMALLINT("smallint", "Integer"), MEDIUMINT("mediumint", "Integer"),
	INT("int", "Integer"), INTEGER("integer", "Integer"), BIGINT("bigint", "Long"), FLOAT("float", "Float"),
	DOUBLE("double", "Double"), DECIMAL("decimal", "BigDecimal"), BIT("bit", "Boolean"), CHAR("char", "String"),
	VARCHAR("varchar", "String"), TINYTEXT("tinytext", "String"), TEXT("text", "String"),
	MEDIUMTEXT("mediumtext", "String"), LONGTEXT("longtext", "String"), DATE("date", "LocalDate"),
	DATETIME("datetime", "LocalDateTime"), TIME("time", "LocalTime"), TIMESTAMP("timestamp", "LocalDateTime"),
	JSON("json", "String");

	private final String columnType;

	private final String javaType;

	/**
	 * 数据库类型转换成java类型
	 */
	@SneakyThrows
	public static String convertJavaType(String columnType) {
		return Arrays.stream(CodeGenColumnTypeEnum.values())
			.filter(e -> Objects.equals(columnType, e.getColumnType()))
			.findFirst()
			.orElseThrow(() -> new FxzException("不支持的数据库字段类型"))
			.getJavaType();
	}

}
