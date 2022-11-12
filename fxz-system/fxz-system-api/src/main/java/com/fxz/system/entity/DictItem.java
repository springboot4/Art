package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.MpEntity;
import lombok.Data;

/**
 * 字典项
 *
 * @author fxz
 * @date 2022-04-04
 */
@Data
@TableName("sys_dict_item")
public class DictItem extends MpEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 字典ID
	 */
	private Long dictId;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 标签
	 */
	private String label;

	/**
	 * 字典类型
	 */
	private String type;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 排序（升序）
	 */
	private Integer sortOrder;

	/**
	 * 备注
	 */
	private String remark;

}
