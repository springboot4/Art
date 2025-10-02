package com.art.gen.core.dto;

import lombok.Data;

/**
 * @author fxz
 * @since 2025/10/1 23:29
 */
@Data
public class DataBaseTablePageDTO {

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
	 * 数据源
	 */
	private String dsName;

	/**
	 * 当前页
	 */
	private int current = 1;

	/**
	 * 每页记录数
	 */
	private int size = 10;

}
