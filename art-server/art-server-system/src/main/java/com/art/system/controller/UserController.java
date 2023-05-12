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

import com.art.common.core.exception.FxzException;
import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.common.security.core.annotation.Ojbk;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.api.user.dto.SystemUserPageDTO;
import com.art.system.api.user.dto.UserInfo;
import com.art.system.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * 用户管理
 *
 * @author fxz
 */
@Tag(name = "用户管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Operation(summary = "通过用户名查找用户信息")
	@Ojbk(inner = true)
	@GetMapping("/findByName/{username}")
	public Result<SystemUserDTO> findByName(@PathVariable("username") String username) {
		return Result.success(this.userService.findByName(username));
	}

	@Operation(summary = "通过手机号查找用户信息")
	@Ojbk(inner = true)
	@GetMapping("/findByMobile/{mobile}")
	public Result<SystemUserDTO> findByMobile(@PathVariable("mobile") String mobile) {
		return Result.success(this.userService.findByMobile(mobile));

	}

	@Operation(summary = "获取当前用户全部信息")
	@GetMapping("/info")
	public Result<UserInfo> userInfo() {
		ArtAuthUser user = SecurityUtil.getUser();
		Assert.notNull(user, "用户未登录！");

		// 查询用户信息
		SystemUserDTO userDTO = userService.getUserById(user.getUserId());

		return Result.success(userService.findUserInfo(userDTO));
	}

	@Operation(summary = "根据id获取用户信息")
	@GetMapping("/getUserById/{id}")
	public Result<SystemUserDTO> getUserById(@PathVariable("id") Long id) {
		return Result.success(userService.getUserById(id));
	}

	@Operation(summary = "分页查询用户信息")
	@GetMapping
	@PreAuthorize("@ps.hasPermission('sys:user:view')")
	public Result<PageResult<SystemUserDTO>> userList(SystemUserPageDTO userPageDTO) {
		return Result.success(PageResult.success(userService.pageUser(userPageDTO)));
	}

	@Operation(summary = "添加用户")
	@PostMapping
	@PreAuthorize("@ps.hasPermission('sys:user:add')")
	public void addUser(@RequestBody SystemUserDTO user) {
		this.userService.createUser(user);
	}

	@Operation(summary = "更新用户")
	@PutMapping
	@PreAuthorize("@ps.hasPermission('sys:user:update')")
	public void updateUser(@RequestBody SystemUserDTO user) {
		this.userService.updateUser(user);
	}

	@Operation(summary = "修改用户基础信息")
	@PutMapping("/info")
	@PreAuthorize("@ps.hasPermission('sys:user:update')")
	public void updateUserInfo(@RequestBody SystemUserDTO user) {
		this.userService.updateUserInfo(user);
	}

	@Operation(summary = "批量删除用户")
	@DeleteMapping("/{userIds}")
	@PreAuthorize("@ps.hasPermission('sys:user:delete')")
	public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws FxzException {
		this.userService.deleteUsers(userIds.split(StringPool.COMMA));
	}

}