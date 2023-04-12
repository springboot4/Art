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

package com.art.system.controller;

import com.art.common.core.model.DeptDataPermissionRespEntity;
import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.common.security.annotation.Ojbk;
import com.art.common.security.entity.ArtAuthUser;
import com.art.common.security.util.SecurityUtil;
import com.art.system.api.role.dto.RoleDTO;
import com.art.system.api.role.dto.RolePageDTO;
import com.art.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 17:47
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	/**
	 * 获取所有角色
	 */
	@Operation(summary = "获取所有角色")
	@GetMapping("/getAllRole")
	public Result<List<RoleDTO>> getAllRole() {
		return Result.success(roleService.getAllRole());
	}

	/**
	 * 分页查询角色信息
	 */
	@Operation(summary = "分页查询角色信息")
	@GetMapping("/page")
	public Result<PageResult<RoleDTO>> pageRole(RolePageDTO pageDTO) {
		return Result.success(PageResult.success(roleService.pageRole(pageDTO)));
	}

	/**
	 * 添加角色
	 */
	@Operation(summary = "添加角色")
	@PostMapping("/addRole")
	public Result<RoleDTO> addRole(@RequestBody RoleDTO roleDTO) {
		return Result.success(roleService.addRole(roleDTO));
	}

	/**
	 * 根据id获取角色信息
	 */
	@Operation(summary = "根据id获取角色信息")
	@GetMapping("/getRoleById/{id}")
	public Result<RoleDTO> getRoleById(@PathVariable("id") Long id) {
		return Result.success(roleService.getRoleById(id));
	}

	/**
	 * 修改角色信息
	 */
	@Operation(summary = "修改角色信息")
	@PutMapping("/editRole")
	public Result<Void> editRole(@RequestBody RoleDTO roleDTO) {
		return Result.judge(roleService.editRole(roleDTO));
	}

	/**
	 * 删除角色信息
	 */
	@Operation(summary = "删除角色信息")
	@DeleteMapping("/deleteRoleById/{id}")
	public Result<Void> deleteRoleById(@PathVariable("id") Long id) {
		return Result.judge(roleService.deleteRoleById(id));
	}

	/**
	 * 获取当前用户角色下的数据权限
	 */
	@Operation(summary = "获取当前用户角色下的数据权限")
	@Ojbk
	@GetMapping("/getDataPermission")
	public Result<DeptDataPermissionRespEntity> getDataPermission() {
		ArtAuthUser user = SecurityUtil.getUser();

		return Result.success(roleService.getDataPermission(user));
	}

}
