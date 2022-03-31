package com.fxz.gen.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据库列信息
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 16:04
 */
@Data
@Accessors(chain = true)
public class DatabaseColumn {

	/**
	 * 行名称
	 */
	private String columnName;

	/**
	 * 数据类型
	 */
	private String dataType;

	/**
	 * 行描述
	 */
	private String columnComment;

	/**
	 * 主键类型
	 */
	private String columnKey;

}
