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

package com.art.common.xxlJob.config;

import com.art.common.xxlJob.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Fxz
 * @version 0.0.1
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
