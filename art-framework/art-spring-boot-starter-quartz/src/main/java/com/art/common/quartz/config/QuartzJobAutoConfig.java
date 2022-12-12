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

package com.art.common.quartz.config;

import com.art.common.quartz.core.aspect.ArtJobLogAspect;
import com.art.common.quartz.core.scheduler.JobScheduler;
import com.art.common.quartz.core.service.ArtJobLogService;
import com.art.common.quartz.core.support.QuartzJobExporter;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 10:43
 */
@AutoConfiguration
public class QuartzJobAutoConfig {

	@Bean
	public QuartzJobExporter quartzJobExporter() {
		return new QuartzJobExporter();
	}

	@Bean
	public JobScheduler jobScheduler(Scheduler scheduler) {
		return new JobScheduler(scheduler);
	}

	@Bean
	public ArtJobLogAspect artJobLogAspect(ArtJobLogService artJobLogService) {
		return new ArtJobLogAspect(artJobLogService);
	}

}
