package com.fxz.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:34
 */
@Data
@TableName("t_dept")
public class Dept implements Serializable {

	private static final long serialVersionUID = -7139055608314923987L;

	/**
	 * 部门id
	 */
	private Long deptId;

	/**
	 * 上级部门id
	 */
	private Long parentId;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 排序
	 */
	private Double orderNum;

	/*
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date modifyTime;

	/**
	 * 子部门id
	 */
	@TableField(exist = false)
	private List<Dept> children;

}
