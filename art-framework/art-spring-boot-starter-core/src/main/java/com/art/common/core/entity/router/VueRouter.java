/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.core.entity.router;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fxz
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VueRouter<T> implements Serializable {

	private static final long serialVersionUID = -3327478146308500708L;

	/**
	 * 路由id
	 */
	private String id;

	/**
	 * 路由父节点id
	 */
	private String parentId;

	/**
	 * 对应路由path
	 */
	private String path;

	/**
	 * 路由名称
	 */
	private String name;

	/**
	 * 对应路由组件component
	 */
	private String component;

	/**
	 * 重定向地址
	 */
	private String redirect;

	/**
	 * 权限
	 */
	private String perms;

	/**
	 * 路由元信息
	 */
	private RouterMeta meta;

	/**
	 * 是否渲染在菜单上
	 */
	private String hidden = "0";

	/**
	 * 类型(0:菜单 1:按钮)
	 */
	private String type;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 是否缓存(0:否 1:是)
	 */
	private String keepAlive;

	/**
	 * 排序
	 */
	private Double orderNum;

	/**
	 * 是否一直显示根路由
	 */
	private Boolean alwaysShow = false;

	/**
	 * title
	 */
	private String title;

	/**
	 * 应用id
	 */
	private Long application;

	/**
	 * 子路由
	 */
	private List<VueRouter<T>> children;

	@JsonIgnore
	private Boolean hasParent = false;

	@JsonIgnore
	private Boolean hasChildren = false;

	public void initChildren() {
		this.children = new ArrayList<>();
	}

}