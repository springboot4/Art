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

package com.art.common.quartz.core.scheduler;

import com.art.common.quartz.core.handler.JobExecuteHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.stereotype.Component;

import static com.art.common.quartz.core.constants.ScheduleConstants.JOB_BEAN_NAME;
import static com.art.common.quartz.core.constants.ScheduleConstants.PARAMETER;
import static com.art.common.quartz.core.utils.QuartzJobUtils.*;

/**
 * job调度
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/6 20:41
 */
@Component
@RequiredArgsConstructor
public class JobScheduler {

	/**
	 * 执行器对象
	 */
	private final Scheduler scheduler;

	/**
	 * 添加job
	 * @param jobId jobId
	 * @param jobGroup job分组
	 * @param jobBeanName jobBean名称
	 * @param jobParam 执行参数
	 * @param cron 执行表达式
	 */
	@SneakyThrows
	public void add(Long jobId, String jobGroup, String jobParam, String jobBeanName, String cron,
			String misfirePolicy) {
		// 构建jobDetail
		JobDetail jobDetail = JobBuilder.newJob(JobExecuteHandler.class)
			.withIdentity(getJobKey(jobId, jobGroup))
			.usingJobData(PARAMETER, jobParam)
			.usingJobData(JOB_BEAN_NAME, jobBeanName)
			.build();

		// 构建执行表达式
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
		cronScheduleBuilder = handleCronScheduleMisfirePolicy(misfirePolicy, cronScheduleBuilder);

		// 构建trigger
		CronTrigger trigger = TriggerBuilder.newTrigger()
			.withIdentity(getTriggerKey(jobId, jobGroup))
			.withSchedule(cronScheduleBuilder)
			.build();

		// 新增调度
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 删除job
	 * @param jobId jobId
	 * @param jobGroup job分组
	 */
	@SneakyThrows
	public void delete(Long jobId, String jobGroup) {
		scheduler.deleteJob(getJobKey(jobId, jobGroup));
	}

	/**
	 * 更新job
	 * @param jobId jobId
	 * @param jobGroup job分组
	 * @param jobParam job执行参数
	 * @param jobBeanName jobBean名称
	 * @param cron 执行表达式
	 * @param misfirePolicy 执行策略
	 */
	@SneakyThrows
	public void update(Long jobId, String jobGroup, String jobParam, String jobBeanName, String cron,
			String misfirePolicy) {
		JobKey jobKey = getJobKey(jobId, jobGroup);
		if (scheduler.checkExists(jobKey)) {
			// 防止创建时存在问题 先移除 再创建
			scheduler.deleteJob(jobKey);
		}

		// 新建job
		add(jobId, jobGroup, jobParam, jobBeanName, cron, misfirePolicy);
	}

	/**
	 * 暂停job
	 * @param jobId jobId
	 * @param jobGroup job分组
	 */
	@SneakyThrows
	public void pause(Long jobId, String jobGroup) {
		scheduler.pauseJob(getJobKey(jobId, jobGroup));
	}

	/**
	 * 恢复job
	 * @param jobId jobId
	 * @param jobGroup job分组
	 */
	@SneakyThrows
	public void resumeJob(Long jobId, String jobGroup) {
		scheduler.resumeJob(getJobKey(jobId, jobGroup));
	}

	/**
	 * 执行一次
	 * @param jobId jobId
	 * @param jobGroup job分组
	 */
	@SneakyThrows
	public void trigger(Long jobId, String jobGroup) {
		scheduler.triggerJob(getJobKey(jobId, jobGroup));
	}

}
