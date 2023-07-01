/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.system.dao.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.art.mybatis.common.base.BaseDelEntity;
import lombok.Data;
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
public class RouteConfDO extends BaseDelEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 主键
	 */
	@JsonIgnore
	@TableId(type = IdType.ASSIGN_ID)
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
