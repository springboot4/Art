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

package com.art.common.log.configuration;

import com.art.common.log.aspect.LogAspect;
import com.art.common.log.service.AsyncLogService;
import com.art.system.feign.RemoteLogService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/25 12:32
 */
@EnableAsync
@AutoConfiguration
public class LogAutoConfiguration {

	@Bean
	public AsyncLogService asyncLogService(RemoteLogService remoteLogService) {
		return new AsyncLogService(remoteLogService);
	}

	@Bean
	public LogAspect logAspect(AsyncLogService asyncLogService) {
		return new LogAspect(asyncLogService);
	}

}
