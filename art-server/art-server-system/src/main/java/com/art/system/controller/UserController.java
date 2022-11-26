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
import com.art.common.core.result.PageResult;
import com.art.common.core.result.Result;
import com.art.common.security.annotation.Ojbk;
import com.art.common.security.entity.FxzAuthUser;
import com.art.common.security.util.SecurityUtil;
import com.art.common.tenant.aspect.IgnoreTenant;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

	/**
	 * 根据id获取用户信息
	 */
	@Operation(summary = "根据id获取用户信息")
	@GetMapping("/getUserById/{id}")
	public Result<SystemUserDTO> getUserById(@PathVariable("id") Long id) {
		return Result.success(userService.getUserById(id));
	}

	/**
	 * 分页查询用户信息
	 */
	@Operation(summary = "分页查询用户信息")
	@GetMapping
	@PreAuthorize("@ps.hasPermission('sys:user:view')")
	public Result<PageResult<SystemUserDTO>> userList(SystemUserPageDTO userPageDTO) {
		return Result.success(PageResult.success(userService.pageUser(userPageDTO)));
	}

	/**
	 * 添加用户
	 */
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

	/**
	 * 删除用户
	 */
	@Operation(summary = "删除用户")
	@DeleteMapping("/{userIds}")
	@PreAuthorize("@ps.hasPermission('sys:user:delete')")
	public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws FxzException {
		String[] ids = userIds.split(StringPool.COMMA);
		this.userService.deleteUsers(ids);
	}

	/**
	 * 通过用户名查找用户信息
	 */
	@Operation(summary = "通过用户名查找用户信息")
	@Ojbk
	@IgnoreTenant
	@GetMapping("/findByName/{username}")
	public SystemUserDTO findByName(@PathVariable("username") String username) {
		return this.userService.findByName(username);
	}

	/**
	 * 通过手机号查找用户信息
	 * @param mobile 手机号
	 * @return 用户信息
	 */
	@Operation(summary = "通过手机号查找用户信息")
	@Ojbk(inner = true)
	@GetMapping("/findByMobile/{mobile}")
	public SystemUserDTO findByMobile(@PathVariable("mobile") String mobile) {
		return this.userService.findByMobile(mobile);

	}

	/**
	 * 获取当前用户全部信息
	 */
	@Operation(summary = "获取当前用户全部信息")
	@GetMapping("/info")
	public Result<UserInfo> userInfo() {
		FxzAuthUser user = SecurityUtil.getUser();

		// 查询用户信息
		SystemUserDTO userDTO = userService.getUserById(user.getUserId());

		return Result.success(userService.findUserInfo(userDTO));
	}

}