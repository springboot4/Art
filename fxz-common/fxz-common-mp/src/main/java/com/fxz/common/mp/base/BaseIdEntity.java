package com.fxz.common.mp.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * 数据库实体继承此类 数据库需含有以下字段
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/25 15:11
 */
@Getter
@Setter
@FieldNameConstants
public class BaseIdEntity implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

}
