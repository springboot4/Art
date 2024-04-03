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

package com.art.system.controller;

import com.art.common.security.core.annotation.Ojbk;
import com.art.core.common.model.Result;
import com.art.system.api.gitee.dto.UsersGiteeDTO;
import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.service.UsersGiteeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 码云Gitee用户表
 *
 * @author fxz
 * @date 2023-04-16
 */
@Tag(name = "码云Gitee用户表")
@RestController
@RequestMapping("/gitee")
@RequiredArgsConstructor
public class UsersGiteeController {

	private final UsersGiteeService sysUsersGiteeService;

	/**
	 * 添加
	 */
	@Ojbk(inner = true)
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody UsersGiteeDTO sysUsersGiteeDTO) {
		return Result.success(sysUsersGiteeService.addSysUsersGitee(sysUsersGiteeDTO));
	}

	/**
	 * 绑定
	 */
	@Ojbk(inner = true)
	@Operation(summary = "绑定")
	@PostMapping(value = "/binding")
	public Result<Boolean> binding(@RequestBody UsersGiteeDTO sysUsersGiteeDTO) {
		return Result.success(sysUsersGiteeService.binding(sysUsersGiteeDTO));
	}

	/**
	 * 更新
	 */
	@Ojbk(inner = true)
	@Operation(summary = "更新")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody UsersGiteeDTO sysUsersGiteeDTO) {
		return Result.success(sysUsersGiteeService.update(sysUsersGiteeDTO));
	}

	/**
	 * 获取单条
	 */
	@Ojbk(inner = true)
	@Operation(summary = "获取单条")
	@GetMapping(value = "/getByAppidAndId")
	Result<UsersGiteeDTO> getByAppidAndId(@RequestParam("appId") String appId, @RequestParam("id") Integer id) {
		return Result.success(sysUsersGiteeService.getByAppidAndId(appId, id));
	}

	/**
	 * 获取用户
	 */
	@Ojbk(inner = true)
	@Operation(summary = "获取用户")
	@GetMapping(value = "/getUser")
	Result<SystemUserDTO> getUser(@RequestParam("appId") String appId, @RequestParam("id") Integer id) {
		return Result.success(sysUsersGiteeService.getUser(appId, id));
	}

}