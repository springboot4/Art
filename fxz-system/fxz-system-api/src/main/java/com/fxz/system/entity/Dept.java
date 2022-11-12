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

package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 18:34
 */
@Data
@TableName("sys_dept")
public class Dept extends BaseCreateEntity implements Serializable {

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

	/**
	 * 子部门id
	 */
	@TableField(exist = false)
	private List<Dept> children;

}
