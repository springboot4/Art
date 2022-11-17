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

package com.art.demos;

import com.art.common.security.annotation.EnableFxzCloudResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/8/30 22:12
 */
@EnableFxzCloudResourceServer
@EnableFeignClients(basePackages = { "com.art" })
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ArtDemosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtDemosApplication.class, args);
	}

}