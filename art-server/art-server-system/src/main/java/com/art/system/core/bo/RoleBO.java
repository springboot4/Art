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

package com.art.system.core.bo;

import com.art.core.common.constant.RoleEnum;
import com.art.json.sdk.util.JacksonUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 19:12
 */
@Data
public class RoleBO implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 角色id
	 */
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

	/**
	 * 角色拥有的菜单id
	 */
	private String menuId;

	private String menuIds;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	public String getMenuId() {
		if (StringUtils.isNoneBlank(menuIds)) {
			return String.join(",", JacksonUtil.parseArray(menuIds, String.class));
		}
		return menuId;
	}

}
