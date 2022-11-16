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

package com.art.gateway.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-12-07 10:22
 */
@Data
@SpringBootConfiguration
@PropertySource(value = { "classpath:fxz-gateway.properties" })
@ConfigurationProperties(prefix = "fxz.gateway")
public class FxzGatewayProperties {

	/**
	 * 禁止外部访问的 URI，多个值的话以逗号分隔
	 */
	private String forbidRequestUri;

}
