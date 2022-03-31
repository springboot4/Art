package com.fxz.gen.enums;

import com.fxz.common.core.exception.FxzException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 16:14
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum CodeGenColumnTypeEnum {

	/**
	 * 数据库字段与java数据类型的映射
	 */
	TINYINT("tinyint", "Integer"), SMALLINT("smallint", "Integer"), MEDIUMINT("mediumint", "Integer"), INT("int",
			"Integer"), INTEGER("integer", "Integer"), BIGINT("bigint", "Long"), FLOAT("float", "Float"), DOUBLE(
					"double", "Double"), DECIMAL("decimal", "BigDecimal"), BIT("bit", "Boolean"), CHAR("char",
							"String"), VARCHAR("varchar", "String"), TINYTEXT("tinytext", "String"), TEXT("text",
									"String"), MEDIUMTEXT("mediumtext", "String"), LONGTEXT("longtext", "String"), DATE(
											"date", "LocalDate"), DATETIME("datetime", "LocalDateTime"), TIME("time",
													"LocalTime"), TIMESTAMP("timestamp", "LocalDateTime");

	private final String columnType;

	private final String javaType;

	/**
	 * 数据库类型转换成java类型
	 */
	@SneakyThrows
	public static String convertJavaType(String columnType) {
		return Arrays.stream(CodeGenColumnTypeEnum.values()).filter(e -> Objects.equals(columnType, e.getColumnType()))
				.findFirst().orElseThrow(() -> new FxzException("不支持的数据库字段类型")).getJavaType();
	}

}
