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

package com.art.common.quartz.core.aspect;

import com.art.common.quartz.core.annotation.ArtQuartzJob;
import com.art.common.quartz.core.service.ArtJobLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 19:37
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class ArtJobLogAspect {

	private final ArtJobLogService artJobLogService;

	@Around("target(com.art.common.quartz.core.job.ArtJob)&&args(String)")
	public Object logPointCut(ProceedingJoinPoint pjp) {
		// 开始时间
		LocalDateTime startTime = LocalDateTime.now();

		Class<?> clazz = pjp.getTarget().getClass();

		ArtQuartzJob quartzJob = clazz.getAnnotation(ArtQuartzJob.class);
		String beanName = quartzJob.name();

		Object o;
		try {
			// 方法执行
			o = pjp.proceed();

			if (quartzJob.log()) {
				// 结束时间
				LocalDateTime endTime = LocalDateTime.now();

				// 日志信息
				String msg = String.format("执行任务:%s,执行耗时:%d毫秒", beanName,
						Duration.between(startTime, endTime).toMillis());

				// 保存日志
				artJobLogService.addJobLog(beanName, msg, null);
			}
		}
		catch (Throwable e) {
			if (quartzJob.log()) {
				artJobLogService.addJobLog(beanName, null, e.getLocalizedMessage());
			}
			throw new RuntimeException(e);
		}
		return o;
	}

}
