package com.fxz.common.core.validator;

/**
 * 参数校验分组
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/29 17:16
 */
public interface ValidationGroup {

	/**
	 * 新增
	 */
	@interface add {

	}

	/**
	 * 更新
	 */
	@interface update {

	}

	/**
	 * 删除
	 */
	@interface delete {

	}

	/**
	 * 查询
	 */
	@interface query {

	}

}
