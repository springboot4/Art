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

package com.art.common.quartz.core.handler;

import com.art.common.quartz.core.job.ArtJob;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.Assert;

import static com.art.common.quartz.core.constants.ScheduleConstants.JOB_BEAN_NAME;
import static com.art.common.quartz.core.constants.ScheduleConstants.PARAMETER;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/6 21:11
 */
@RequiredArgsConstructor
public class JobExecuteHandler extends QuartzJobBean {

	private final ApplicationContext applicationContext;

	private String jobBeanName;

	private String parameter;

	@Override
	protected void executeInternal(JobExecutionContext context) {
		jobBeanName = context.getMergedJobDataMap().getString(JOB_BEAN_NAME);
		parameter = context.getMergedJobDataMap().getString(PARAMETER);

		this.doExecute();
	}

	@SneakyThrows
	private void doExecute() {
		ArtJob job = applicationContext.getBean(jobBeanName, ArtJob.class);
		Assert.notNull(job, "job不存在！");

		job.execute(parameter);
	}

}
