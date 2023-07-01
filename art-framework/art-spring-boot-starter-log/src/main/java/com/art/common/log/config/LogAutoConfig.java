/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.log.config;

import com.art.common.log.core.aspect.LogAspect;
import com.art.common.log.core.event.SysLogListener;
import com.art.common.log.core.service.AsyncLogService;
import com.art.system.api.log.LogServiceApi;
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
public class LogAutoConfig {

	@Bean
	public AsyncLogService asyncLogService(LogServiceApi logServiceApi) {
		return new AsyncLogService(logServiceApi);
	}

	@Bean
	public LogAspect logAspect(AsyncLogService asyncLogService) {
		return new LogAspect(asyncLogService);
	}

	@Bean
	public SysLogListener sysLogListener(AsyncLogService asyncLogService) {
		return new SysLogListener(asyncLogService);
	}

}
