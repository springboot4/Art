package com.fxz.common.log.configuration;

import com.fxz.common.log.aspect.LogAspect;
import com.fxz.common.log.service.AsyncLogService;
import com.fxz.system.feign.RemoteLogService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/25 12:32
 */
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
