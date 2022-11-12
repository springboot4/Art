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

package com.fxz.common.canal.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author fxz
 * @description binlog事件类型
 */
@RequiredArgsConstructor
@Getter
public enum BinLogEventType {

	/**
	 * 查询
	 */
	QUERY("QUERY", "查询"),

	/**
	 * 新增
	 */
	INSERT("INSERT", "新增"),

	/**
	 * 更新
	 */
	UPDATE("UPDATE", "更新"),

	/**
	 * 删除
	 */
	DELETE("DELETE", "删除"),

	/**
	 * 列修改操作
	 */
	ALTER("ALTER", "列修改操作"),

	/**
	 * 表创建
	 */
	CREATE("CREATE", "表创建"),

	/**
	 * 未知
	 */
	UNKNOWN("UNKNOWN", "未知");

	private final String type;

	private final String description;

	public static BinLogEventType fromType(String type) {
		for (BinLogEventType binLogType : BinLogEventType.values()) {
			if (binLogType.getType().equals(type)) {
				return binLogType;
			}
		}
		return BinLogEventType.UNKNOWN;
	}

}
