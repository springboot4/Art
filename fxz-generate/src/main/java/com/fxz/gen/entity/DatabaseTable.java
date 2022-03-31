package com.fxz.gen.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 数据库表基本信息
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 15:58
 */
@Data
@Accessors(chain = true)
public class DatabaseTable {

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 引擎类型
	 */
	private String engine;

	/**
	 * 表表述
	 */
	private String tableComment;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime createTime;

}
