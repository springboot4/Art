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

package com.art.scheduled.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.art.common.core.result.PageResult;
import com.art.common.core.result.Result;
import com.art.scheduled.core.entity.JobLog;
import com.art.scheduled.service.JobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务调度日志
 *
 * @author fxz
 * @date 2022-04-03
 */
@RestController
@RequestMapping("/jobLog")
@RequiredArgsConstructor
public class JobLogController {

	private final JobLogService jobLogService;

	@PostMapping(value = "/add")
	public Result add(@RequestBody JobLog param) {
		return Result.success(jobLogService.add(param));
	}

	@PostMapping(value = "/update")
	public Result update(@RequestBody JobLog param) {
		return Result.success(jobLogService.update(param));
	}

	@DeleteMapping(value = "/delete")
	public Result delete(Long id) {
		return Result.judge(true);
	}

	@GetMapping(value = "/findById")
	public Result findById(Long id) {
		return Result.success(jobLogService.findById(id));
	}

	@GetMapping(value = "/findAll")
	public Result findAll() {
		return Result.success(jobLogService.findAll());
	}

	@GetMapping(value = "/page")
	public Result<PageResult<JobLog>> page(Page<JobLog> page, JobLog jobLog) {
		return Result.success(jobLogService.page(page, jobLog));
	}

}