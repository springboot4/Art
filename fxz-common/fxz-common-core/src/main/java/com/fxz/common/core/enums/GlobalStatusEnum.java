package com.fxz.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 16:18
 */
@Getter
@AllArgsConstructor
public enum GlobalStatusEnum {

	/**
	 * 开启状态
	 */
	ENABLE(0, "开启"),

	/**
	 * 关闭状态
	 */
	DISABLE(1, "关闭");

	/**
	 * 状态值
	 */
	private final Integer value;

	/**
	 * 状态描述
	 */
	private final String label;

}
