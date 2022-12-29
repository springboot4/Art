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

package com.art.common.quartz.core.utils;

import com.art.common.quartz.core.constants.ScheduleConstants;
import lombok.experimental.UtilityClass;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/6 20:47
 */
@UtilityClass
public class QuartzJobUtils {

	/**
	 * 生成任务键对象key
	 */
	public static JobKey getJobKey(Long jobId, String jobGroup) {
		return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
	}

	/**
	 * 生成任务触发对象key
	 */
	public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
		return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
	}

	/**
	 * 定时任务执行策略
	 */
	public static CronScheduleBuilder handleCronScheduleMisfirePolicy(String misfirePolicy, CronScheduleBuilder cb) {
		switch (misfirePolicy) {
			case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
				// 立即触发执行
				return cb.withMisfireHandlingInstructionIgnoreMisfires();
			case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
				// 触发一次执行
				return cb.withMisfireHandlingInstructionFireAndProceed();
			case ScheduleConstants.MISFIRE_DO_NOTHING:
				// 不触发立即执行
				return cb.withMisfireHandlingInstructionDoNothing();
			case ScheduleConstants.MISFIRE_DEFAULT:
			default:
				// 默认触发策略
				return cb;
		}
	}

}
