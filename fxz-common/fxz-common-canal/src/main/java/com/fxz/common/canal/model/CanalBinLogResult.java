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

package com.fxz.common.canal.model;

import com.fxz.common.canal.common.BinLogEventType;
import com.fxz.common.canal.common.OperationType;
import lombok.Data;

/**
 * @author fxz
 */
@Data
public class CanalBinLogResult<T> {

	/**
	 * 提取的长整型主键
	 */
	private Long primaryKey;

	/**
	 * binlog事件类型
	 */
	private BinLogEventType binLogEventType;

	/**
	 * 更变前的数据
	 */
	private T beforeData;

	/**
	 * 更变后的数据
	 */
	private T afterData;

	/**
	 * 数据库名称
	 */
	private String databaseName;

	/**
	 * 表名称
	 */
	private String tableName;

	/**
	 * sql语句 - 一般是DDL的时候有用
	 */
	private String sql;

	/**
	 * mysql操作类型
	 */
	private OperationType operationType;

}
