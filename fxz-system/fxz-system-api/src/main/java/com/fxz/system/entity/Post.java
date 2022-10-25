package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 岗位信息
 *
 * @author fxz
 * @date 2022-04-05
 */
@Data
@TableName("sys_post")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Post extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 岗位ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long postId;

	/**
	 * 岗位编码
	 */
	private String postCode;

	/**
	 * 岗位名称
	 */
	private String postName;

	/**
	 * 岗位排序
	 */
	private Integer postSort;

	/**
	 * 是否删除 -1：已删除 0：正常
	 */
	@TableLogic
	private String delFlag;

	/**
	 * 描述
	 */
	private String description;

}
