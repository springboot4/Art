package com.fxz.common.xxlJob.config;

import com.fxz.common.xxlJob.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/13 20:19
 */
@Slf4j
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobConfig {

	@Bean
	public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties xxlJobProperties) {
		log.info(">>>>>>>>>>> xxl-job config init.");
		log.info(">>>>>>>>>>> xxl-job properties:{}", xxlJobProperties);

		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
		xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppname());
		xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
		xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
		xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
		xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
		xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
		xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());

		return xxlJobSpringExecutor;
	}

}
