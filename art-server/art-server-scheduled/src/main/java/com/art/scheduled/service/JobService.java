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

package com.art.scheduled.service;

import com.art.common.core.result.PageResult;
import com.art.common.quartz.core.constants.ScheduleConstants;
import com.art.common.quartz.core.scheduler.JobScheduler;
import com.art.scheduled.core.entity.SysJob;
import com.art.scheduled.dao.mysql.JobMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度表
 *
 * @author fxz
 * @date 2022-04-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

	private final JobMapper jobMapper;

	private final JobScheduler jobScheduler;

	/**
	 * 添加任务
	 */
	@SneakyThrows
	public SysJob add(SysJob sysJob) {
		// 保存数据库
		jobMapper.insert(sysJob);

		// 创建定时任务
		jobScheduler.add(sysJob.getJobId(), sysJob.getJobGroup(), sysJob.getParameters(), sysJob.getJobName(),
				sysJob.getCronExpression(), sysJob.getMisfirePolicy());

		// 更改job状态
		changeStatus(sysJob.getJobId(), sysJob.getJobGroup(), sysJob.getStatus());

		return sysJob;
	}

	/**
	 * 更新任务
	 */
	@SneakyThrows
	public SysJob update(SysJob sysJob) {
		jobMapper.updateById(sysJob);

		jobScheduler.update(sysJob.getJobId(), sysJob.getJobGroup(), sysJob.getParameters(), sysJob.getJobName(),
				sysJob.getCronExpression(), sysJob.getMisfirePolicy());

		// 更改job状态
		changeStatus(sysJob.getJobId(), sysJob.getJobGroup(), sysJob.getStatus());

		return sysJob;
	}

	/**
	 * 根据id删除任务
	 */
	@SneakyThrows
	public Boolean deleteByJobId(Long id) {
		SysJob sysJob = jobMapper.selectById(id);
		int count = jobMapper.deleteById(id);
		if (count > 0) {
			jobScheduler.delete(sysJob.getJobId(), sysJob.getJobGroup());
		}
		return jobMapper.deleteById(id) > 0;
	}

	/**
	 * 定时任务状态修改
	 */
	@SneakyThrows
	public Boolean changeStatus(SysJob job) {
		// 更新数据库
		int rows = jobMapper.updateById(job);

		// 更改job状态
		changeStatus(job.getJobId(), job.getJobGroup(), job.getStatus());

		return rows > 0;
	}

	private void changeStatus(Long jobId, String jobGroup, String status) {
		if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
			resumeJob(jobId, jobGroup);
		}
		else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
			pauseJob(jobId, jobGroup);
		}
	}

	/**
	 * 暂停任务
	 * @param jobId jobId
	 * @param jobGroup job分组
	 */
	public void pauseJob(Long jobId, String jobGroup) {
		jobScheduler.pause(jobId, jobGroup);
	}

	/**
	 * 恢复任务
	 * @param jobId jobId
	 * @param jobGroup job分组
	 */
	public void resumeJob(Long jobId, String jobGroup) {
		jobScheduler.resumeJob(jobId, jobGroup);
	}

	/**
	 * 定时任务立即执行一次
	 */
	@SneakyThrows
	public void run(SysJob job) {
		jobScheduler.trigger(job.getJobId(), job.getJobGroup());
	}

	/**
	 * 分页
	 */
	public PageResult<SysJob> page(Page<SysJob> page, SysJob sysJob) {
		Page<SysJob> pageResult = jobMapper.selectPage(page, Wrappers.<SysJob>lambdaQuery()
				.like(StringUtils.isNotBlank(sysJob.getJobName()), SysJob::getJobName, sysJob.getJobName()));
		return PageResult.success(pageResult);
	}

	/**
	 * 获取单条
	 */
	public SysJob findById(Long id) {
		return jobMapper.selectById(id);
	}

}