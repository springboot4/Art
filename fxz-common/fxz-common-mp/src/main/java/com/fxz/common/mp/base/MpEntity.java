package com.fxz.common.mp.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据库实体继承此类 数据库需含有以下字段
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/25 15:17
 */
@Getter
@Setter
@FieldNameConstants
public class MpEntity implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

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

	/**
	 * 逻辑删除标志
	 */
	@TableLogic
	private boolean delFlag;

}
