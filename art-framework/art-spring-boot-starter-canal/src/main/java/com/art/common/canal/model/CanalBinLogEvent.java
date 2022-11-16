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

package com.art.common.canal.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author fxz
 */
@Data
public class CanalBinLogEvent {

	/**
	 * 事件ID,没有实际意义
	 */
	private Long id;

	/**
	 * 当前更变前节点数据
	 */
	private List<Map<String, String>> old;

	/**
	 * 当前更变后节点数据
	 */
	private List<Map<String, String>> data;

	/**
	 * 主键行名称列表
	 */
	private List<String> pkNames;

	/**
	 * 类型 UPDATE\INSERT\DELETE\QUERY
	 */
	private String type;

	/**
	 * binlog execute time
	 */
	private Long es;

	/**
	 * dml build timestamp
	 */
	private Long ts;

	/**
	 * 执行的sql,不一定存在
	 */
	private String sql;

	/**
	 * 数据库名称
	 */
	private String database;

	/**
	 * 表名称
	 */
	private String table;

	/**
	 * SQL类型映射
	 */
	private Map<String, Integer> sqlType;

	/**
	 * MySQL字段类型映射
	 */
	private Map<String, String> mysqlType;

	/**
	 * 是否DDL
	 */
	private Boolean isDdl;

}
