package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * 路由配置表
 *
 * @author fxz
 * @date 2022-08-20
 */
@Data
@FieldNameConstants
@TableName("sys_route_conf")
@Accessors(chain = true)
public class RouteConf {

	private static final long serialVersionUID = -1L;

	/**
	 * 主键
	 */
	@JsonIgnore
	private Long id;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 路由id
	 */
	private String routeId;

	/**
	 * 断言
	 */
	private String predicates;

	/**
	 * 过滤器
	 */
	private String filters;

	/**
	 * uri
	 */
	private String uri;

	/**
	 * 排序
	 */
	private Integer sortOrder;

	/**
	 * 路由元信息
	 */
	private String metadata;

	/**
	 * 删除标志
	 */
	@TableLogic
	private String delFlag;

	/**
	 * 创建者
	 */
	@JsonIgnore
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@JsonIgnore
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	@JsonIgnore
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@JsonIgnore
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
