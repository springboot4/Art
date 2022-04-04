package com.fxz.gen.service;

import com.fxz.gen.entity.DatabaseColumn;
import com.fxz.gen.entity.DatabaseTable;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 15:55
 */
public interface DatabaseTableService {

	/**
	 * 获取表基本信息
	 */
	DatabaseTable findByTableName(String tableName, String dsName);

	/**
	 * 获取表的列信息
	 */
	List<DatabaseColumn> findColumnByTableName(String tableName, String dsName);

}
