package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.BaseIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
@Data
@TableName("sys_app")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class App extends BaseIdEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 应用名称
	 */
	private String name;

	/**
	 * 应用编码
	 */
	private String code;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer sort;

}
