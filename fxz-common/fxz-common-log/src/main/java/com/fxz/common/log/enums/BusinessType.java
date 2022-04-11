package com.fxz.common.log.enums;

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
