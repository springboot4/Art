package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.MpEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典表
 *
 * @author fxz
 * @date 2022-04-04
 */
@Data
@TableName("sys_dict")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Dict extends MpEntity {

	private static final long serialVersionUID = -7407384936260772014L;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 是否是系统内置
	 */
	private String systemFlag;

}
