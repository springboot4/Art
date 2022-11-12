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

package com.fxz.auth.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 21:00
 */
@Data
@SpringBootConfiguration
@PropertySource(value = { "classpath:fxz-auth.properties" })
@ConfigurationProperties(prefix = "fxz.auth")
public class FxzAuthProperties {

	private int accessTokenValiditySeconds = 60 * 60 * 24;

	private int refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

	/**
	 * 免认证路径
	 */
	private String anonUrl;

	/**
	 * 验证码配置类
	 */
	private FxzValidateCodeProperties code = new FxzValidateCodeProperties();

}
