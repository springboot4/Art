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

import com.art.core.common.constant.RoleEnum;
import com.art.common.mp.core.base.BaseCreateEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 17:48
 */
@Data
@TableName("sys_role")
public class RoleDO extends BaseCreateEntity implements Serializable {

	private static final long serialVersionUID = -1189610678764394766L;

	/**
	 * 角色id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色code {@link RoleEnum#getType()}
	 */
	private String code;

	/**
	 * 角色描述
	 */
	private String remark;

	/**
	 * 数据权限范围
	 */
	private Integer dataScope;

	/**
	 * 数据范围(指定部门数组)
	 */
	private String dataScopeDeptIds;

	/**
	 * 租户id
	 */
	private Long tenantId;

}
