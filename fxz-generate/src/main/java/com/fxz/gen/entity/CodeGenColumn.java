package com.fxz.gen.entity;

import lombok.Data;

/**
 * 代码生成列信息
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 16:07
 */
@Data
public class CodeGenColumn {

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private String attrType;

	/**
	 * 备注
	 */
	private String comments;

}
