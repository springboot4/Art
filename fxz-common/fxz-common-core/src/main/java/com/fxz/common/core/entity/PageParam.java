package com.fxz.common.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 12:40
 */
@Data
public class PageParam implements Serializable {

	private static final long serialVersionUID = -6136931343132969615L;

	/**
	 * 当前页
	 */
	private final int current = 1;

	/**
	 * 每页记录数
	 */
	private final int size = 10;

}
