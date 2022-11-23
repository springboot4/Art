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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.art.common.mp.result.PageResult;
import com.art.common.mp.result.Result;
import com.art.system.api.user.UserPostDTO;
import com.art.system.dao.dataobject.UserPostDO;
import com.art.system.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户与岗位关联表
 *
 * @author fxz
 * @date 2022-04-05
 */
@Tag(name = "用户岗位管理")
@RestController
@RequestMapping("/userPost")
@RequiredArgsConstructor
public class UserPostController {

	private final UserPostService userPostService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody UserPostDTO userPostDto) {
		return Result.success(userPostService.addUserPost(userPostDto));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody UserPostDTO userPostDto) {
		return Result.success(userPostService.updateUserPost(userPostDto));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(userPostService.deleteUserPost(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<UserPostDO> findById(Long id) {
		return Result.success(userPostService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<UserPostDO>> findAll() {
		return Result.success(userPostService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<UserPostDO>> pageUserPost(Page<UserPostDO> pageParam, UserPostDO userPostDO) {
		return Result.success(PageResult.success(userPostService.pageUserPost(pageParam, userPostDO)));
	}

}