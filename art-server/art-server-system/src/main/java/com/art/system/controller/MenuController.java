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

import com.art.common.core.model.VueRouter;
import com.art.common.core.exception.FxzException;
import com.art.common.core.model.Result;
import com.art.common.security.core.annotation.Ojbk;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.system.api.dict.dto.MenuDTO;
import com.art.system.dao.dataobject.MenuDO;
import com.art.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 菜单管理
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-29 18:47
 */
@Tag(name = "菜单管理")
@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	/**
	 * 获取用户路由信息和用户权限信息
	 */
	@Operation(summary = "获取用户路由信息和用户权限信息")
	@GetMapping("/nav")
	public Result<Map<String, Object>> getUserRoutersAndAuthority() {
		return Result.success(menuService.getUserRoutersAndAuthority());
	}

	/**
	 * 获取用户角色下的所有树形菜单信息(包括按钮)
	 * @return 用户角色下的树形菜单信息
	 */
	@Operation(summary = "获取用户角色下的所有树形菜单信息(包括按钮)")
	@GetMapping("/getUserMenuTree")
	public Result<List<VueRouter<MenuDTO>>> getUserMenuTree() {
		ArtAuthUser authUser = Optional.ofNullable(SecurityUtil.getUser())
			.orElseThrow(() -> new FxzException("用户未登录！"));

		return Result.success(menuService.getUserMenuTree(authUser.getUserId()));
	}

	/**
	 * 获取全部的树形菜单信息(包括按钮)
	 * @return 树形菜单信息
	 */
	@Operation(summary = "获取全部的树形菜单信息(包括按钮)")
	@GetMapping("/getAllMenuTree")
	public Result<List<VueRouter<MenuDTO>>> getAllMenuTree() {
		return Result.success(this.menuService.getAllMenuTree());
	}

	/**
	 * 获取菜单下拉框
	 * @return 树形菜单下拉框
	 */
	@Operation(summary = "获取菜单下拉框")
	@GetMapping("/getTreeSelect")
	public Result<List<VueRouter<MenuDTO>>> getTreeSelect() {
		return Result.success(this.menuService.getTreeSelect());
	}

	/**
	 * 保存路由信息
	 * @param vueRouter 路由信息
	 */
	@Operation(summary = "保存路由信息")
	@PostMapping("/save")
	public void saveMenu(@RequestBody VueRouter vueRouter) {
		this.menuService.saveMenu(vueRouter);
	}

	/**
	 * 根据id删除路由信息
	 * @param id
	 */
	@Operation(summary = "根据id删除路由信息")
	@DeleteMapping("/delete/{id}")
	public void deleteMenu(@PathVariable("id") Long id) {
		this.menuService.removeById(id);
	}

	/**
	 * 根据id查询路由信息
	 * @param id
	 * @return
	 */
	@Operation(summary = "根据id查询路由信息")
	@GetMapping("/getMenuById/{id}")
	public Result<VueRouter<MenuDO>> getMenuById(@PathVariable("id") Long id) {
		return Result.success(this.menuService.getMenuById(id));
	}

	/**
	 * 更新路由
	 */
	@Operation(summary = "更新路由")
	@PostMapping("/update")
	public void updateMenu(@RequestBody VueRouter vueRouter) {
		this.menuService.updateMenu(vueRouter);
	}

	/**
	 * 通过用户名查询权限信息
	 * @param username 用户名称
	 * @return 权限信息
	 */
	@Operation(summary = "通过用户名查询权限信息")
	@Ojbk(inner = true)
	@GetMapping("/findUserPermissions/{username}")
	public Result<Set<String>> findUserPermissions(@PathVariable("username") String username) {
		return Result.success(menuService.findUserPermissions(username));
	}

}
