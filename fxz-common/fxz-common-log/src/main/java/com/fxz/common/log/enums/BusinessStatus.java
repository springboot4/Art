package com.fxz.common.log.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 操作状态
 *
 * @author fxz
 */
@Getter
@RequiredArgsConstructor
public enum BusinessStatus {

	/**
	 * 成功
	 */
	SUCCESS(0, "正常"),

	/**
	 * 失败
	 */
	FAIL(1, "失败");

	/**
	 * 类型
	 */
	private final Integer value;

	/**
	 * 描述
	 */
	private final String label;

}
