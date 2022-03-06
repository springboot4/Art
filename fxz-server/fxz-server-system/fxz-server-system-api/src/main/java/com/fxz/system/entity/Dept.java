package com.fxz.system.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
	@TableId(type = IdType.AUTO)
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
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Date modifyTime;

	/**
	 * 子部门id
	 */
	@TableField(exist = false)
	private List<Dept> children;

}
