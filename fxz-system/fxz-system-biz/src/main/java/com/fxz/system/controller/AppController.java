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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.core.util.ValidationUtil;
import com.fxz.common.core.validator.ValidationGroup;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.system.entity.App;
import com.fxz.system.param.AppParam;
import com.fxz.system.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
@Tag(name = "应用管理")
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppController {

	private final AppService appService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody App app) {
		ValidationUtil.validateParam(app, ValidationGroup.add.class);
		return Result.success(appService.addSysApp(app));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody App app) {
		ValidationUtil.validateParam(app, ValidationGroup.update.class);
		return Result.success(appService.updateSysApp(app));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(appService.deleteSysApp(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<App> findById(Long id) {
		return Result.success(appService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<App>> findAll() {
		return Result.success(appService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<App>> pageSysApp(Page<App> pageParam, AppParam appParam) {
		return Result.success(PageResult.success(appService.pageSysApp(pageParam, appParam)));
	}

}