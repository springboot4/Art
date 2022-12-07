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

import cn.hutool.core.lang.Assert;
import com.art.common.core.result.PageResult;
import com.art.common.core.result.Result;
import com.art.common.quartz.core.utils.CronUtils;
import com.art.scheduled.core.entity.SysJob;
import com.art.scheduled.service.JobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务调度
 *
 * @author fxz
 * @date 2022-04-03
 */
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

	private final JobService jobService;

	@PostMapping(value = "/add")
	public Result<SysJob> add(@RequestBody SysJob sysJob) {
		validJob(sysJob);
		return Result.success(jobService.add(sysJob));
	}

	@PostMapping(value = "/update")
	public Result<SysJob> update(@RequestBody SysJob sysJob) {
		validJob(sysJob);
		return Result.success(jobService.update(sysJob));
	}

	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(jobService.deleteByJobId(id));
	}

	@GetMapping(value = "/findById")
	public Result<SysJob> findById(Long id) {
		return Result.success(jobService.findById(id));
	}

	@GetMapping(value = "/page")
	public Result<PageResult<SysJob>> page(Page<SysJob> page, SysJob sysJob) {
		return Result.success(jobService.page(page, sysJob));
	}

	/**
	 * 定时任务状态修改
	 */
	@PutMapping("/changeStatus")
	public Result<Boolean> changeStatus(@RequestBody SysJob job) {
		return Result.success(jobService.changeStatus(job));
	}

	/**
	 * 定时任务立即执行一次
	 */
	@PutMapping("/run")
	public Result<Void> run(@RequestBody SysJob job) {
		jobService.run(job);
		return Result.success();
	}

	private void validJob(SysJob sysJob) {
		Assert.isTrue(CronUtils.isValid(sysJob.getCronExpression()), "新增任务'" + sysJob.getJobName() + "'失败，Cron表达式不正确");
	}

}