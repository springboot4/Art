package com.fxz.common.gen.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

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
@FieldNameConstants
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
	private LocalDateTime createTime;

}
