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

package com.fxz.system.controller;

import com.fxz.common.core.entity.router.VueRouter;
import com.fxz.common.core.exception.FxzException;
import com.fxz.common.mp.result.Result;
import com.fxz.common.security.annotation.Ojbk;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import com.fxz.system.entity.Menu;
import com.fxz.system.entity.SystemUser;
import com.fxz.system.service.AppService;
import com.fxz.system.service.IMenuService;
import com.fxz.system.service.IRoleService;
import com.fxz.system.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 菜单管理
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-29 18:47
 */
@Tag(name = "菜单管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

	private final IMenuService menuService;

	private final IRoleService roleService;

	private final IUserService userService;

	private final AppService appService;

	/**
	 * 获取用户路由信息和用户权限信息
	 */
	@Operation(summary = "获取用户路由信息和用户权限信息")
	@GetMapping("/nav")
	public Result<Map<String, Object>> getUserRouters() {
		// 返回结果集
		Map<String, Object> result = new HashMap<>(4);

		Optional.ofNullable(SecurityUtil.getUser()).map(FxzAuthUser::getUsername).ifPresent(userName -> {
			// 构建用户路由对象
			CompletableFuture<Void> routes = CompletableFuture
					.runAsync(() -> result.put("routes", this.menuService.getUserRouters(userName)));
			// 封装用户权限信息
			CompletableFuture<Void> permissions = CompletableFuture
					.runAsync(() -> result.put("permissions", SecurityUtil.getUser().getAuthorities().toArray()));
			// 封装应用信息
			CompletableFuture<Void> apps = CompletableFuture
					.runAsync(() -> result.put("apps", this.appService.findAll()));

			// 异步执行
			CompletableFuture.allOf(routes, permissions, apps).join();
		});

		return Result.success(result);
	}

	/**
	 * 获取用户角色下的所有树形菜单信息(包括按钮)
	 * @return 用户角色下的树形菜单信息
	 */
	@Operation(summary = "获取用户角色下的所有树形菜单信息(包括按钮)")
	@GetMapping("/getUserMenuTree")
	public Result<List<VueRouter<Menu>>> getUserMenuTree() {
		FxzAuthUser authUser = SecurityUtil.getUser();
		if (Objects.isNull(authUser)) {
			throw new FxzException("用户未登录！");
		}

		SystemUser user = userService.getUserById(authUser.getUserId());
		if (roleService.isSuperAdmin(user.getRoleId())) {
			return Result.success(this.menuService.getAllMenuTree());
		}
		else {
			return Result.success(this.menuService.getUserRouters(user.getUsername()));
		}
	}

	/**
	 * 获取全部的树形菜单信息(包括按钮)
	 * @return 树形菜单信息
	 */
	@Operation(summary = "获取全部的树形菜单信息(包括按钮)")
	@GetMapping("/getAllMenuTree")
	public Result<List<VueRouter<Menu>>> getAllMenuTree() {
		return Result.success(this.menuService.getAllMenuTree());
	}

	/**
	 * 获取菜单下拉框
	 * @return 树形菜单下拉框
	 */
	@Operation(summary = "获取菜单下拉框")
	@GetMapping("/getTreeSelect")
	public Result<List<VueRouter<Menu>>> getTreeSelect() {
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
	public Result<VueRouter> getMenuById(@PathVariable("id") Long id) {
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
	public Set<String> findUserPermissions(@PathVariable("username") String username) {
		return menuService.findUserPermissions(username);
	}

}
