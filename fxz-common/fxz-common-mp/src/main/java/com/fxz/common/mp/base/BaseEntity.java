package com.fxz.common.mp.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 所有数据库实体均继承此类 数据库均含有以下审计字段
 *
 * @author fxz
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 7857880311870200278L;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}