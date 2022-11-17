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
import com.art.common.security.annotation.Ojbk;
import com.art.system.dto.OperLogDto;
import com.art.system.entity.OperLog;
import com.art.system.service.OperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author fxz
 * @date 2022-04-07
 */
@Tag(name = "操作日志管理")
@RestController
@RequestMapping("/operLog")
@RequiredArgsConstructor
public class OperLogController {

	private final OperLogService operLogService;

	/**
	 * 保存日志
	 */
	@Operation(summary = "保存日志")
	@Ojbk
	@PostMapping(value = "/add")
	public void add(@RequestBody OperLogDto operLogDto) {
		operLogService.addOperLog(operLogDto);
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody OperLogDto operLogDto) {
		return Result.success(operLogService.updateOperLog(operLogDto));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(operLogService.deleteOperLog(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<OperLog> findById(Long id) {
		return Result.success(operLogService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<OperLog>> findAll() {
		return Result.success(operLogService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<OperLog>> pageOperLog(Page<OperLog> pageParam, OperLog operLog) {
		return Result.success(PageResult.success(operLogService.pageOperLog(pageParam, operLog)));
	}

}