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

package com.art.system.service;

import com.art.common.core.model.DeptDataPermissionRespEntity;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.system.api.role.dto.RoleDTO;
import com.art.system.api.role.dto.RolePageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 17:47
 */
public interface RoleService {

	/**
	 * 分页查询角色信息
	 */
	IPage<RoleDTO> pageRole(RolePageDTO pageDTO);

	/**
	 * 添加角色信息
	 */
	RoleDTO addRole(RoleDTO roleDTO);

	/**
	 * 根据id获取角色信息
	 */
	RoleDTO getRoleById(Long id);

	/**
	 * 修改角色信息
	 */
	Boolean editRole(RoleDTO roleDTO);

	/**
	 * 删除角色信息
	 */
	Boolean deleteRoleById(Long id);

	/**
	 * 获取当前用户角色下的数据权限
	 */
	DeptDataPermissionRespEntity getDataPermission(ArtAuthUser user);

	/**
	 * 是否是超级管理员
	 * @param roleId 角色id
	 */
	boolean isSuperAdmin(String roleId);

	/**
	 * 获取所有角色
	 */
	List<RoleDTO> getAllRole();

}
