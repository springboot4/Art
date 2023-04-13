/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.core.support;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/15 14:58
 */
@Slf4j
public class CustomizerBanner implements ApplicationRunner {

	private final String doc = "http://art-gateway:9999";

	private final String bootVersion = SpringBootVersion.getVersion();

	private final String name;

	private final String osName;

	private final String osArch;

	private final String osVersion;

	private final String javaVendor;

	private final String javaVersion;

	public CustomizerBanner(Environment environment) {
		name = environment.getProperty("spring.application.name");
		osName = environment.getProperty("os.name");
		osArch = environment.getProperty("os.arch");
		osVersion = environment.getProperty("os.version");
		javaVendor = environment.getProperty("java.vendor");
		javaVersion = environment.getProperty("java.version");
	}

	@Override
	public void run(ApplicationArguments args) {
		ThreadUtil.execute(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			log.info("操作系统：{},{},{}", osName, osArch, osVersion);
			log.info("Java环境：{},{}", javaVendor, javaVersion);
			log.info("SpringBoot版本：{}", bootVersion);
			log.info("项目启动成功！服务:{} ", name);
			log.info("项目文档相关:{}", doc);
		});
	}

}