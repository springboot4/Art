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

package com.fxz.common.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;

import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/15 14:58
 */
@RequiredArgsConstructor
@Slf4j
public class BannerRunner implements ApplicationRunner {

	private final ServiceInstance serviceInstance;

	@Override
	public void run(ApplicationArguments args) {
		ThreadUtil.execute(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			String serviceId = serviceInstance.getServiceId();
			log.info("" + "\n" + "项目启动成功！😊" + "\n" + "服务:{}" + "\n" + "接口文档:http://fxz-gateway:8301/doc.html",
					serviceId);
		});
	}

}
