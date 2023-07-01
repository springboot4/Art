/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.log.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务操作类型
 *
 * @author fxz
 */
@Getter
@AllArgsConstructor
public enum BusinessType {

	/**
	 * 其它
	 */
	OTHER(0, "其他"),

	/**
	 * 新增
	 */
	INSERT(1, "新增"),

	/**
	 * 修改
	 */
	UPDATE(2, "修改"),

	/**
	 * 删除
	 */
	DELETE(3, "删除"),

	/**
	 * 登录
	 */
	GRANT(4, "登录"),

	/**
	 * 导出
	 */
	EXPORT(5, "导出"),

	/**
	 * 导入
	 */
	IMPORT(6, "导入"),

	/**
	 * 强退
	 */
	FORCE(7, "强退"),

	/**
	 * 生成代码
	 */
	GENCODE(8, "生成代码"),

	/**
	 * 清空数据
	 */
	CLEAN(9, "清空数据");

	private final Integer value;

	private final String label;

}
