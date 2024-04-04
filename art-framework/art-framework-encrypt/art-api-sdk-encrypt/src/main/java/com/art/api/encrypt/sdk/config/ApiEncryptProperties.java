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

package com.art.api.encrypt.sdk.config;

import com.art.api.encrypt.sdk.constants.EncryptType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2024/4/4 15:17
 */
@Data
@ConfigurationProperties(ApiEncryptProperties.PREFIX)
public class ApiEncryptProperties {

	public static final String PREFIX = "art.api.encrypt";

	/**
	 * 请求中加密参数的key 对于GET请求来说是:
	 * <p/>
	 * ?encryption=xxx 其中encryption就是key xxx就是加密后的请求参数: {"username": "admin", "password":
	 * "123456"}
	 * <p/>
	 * 对于POST请求来说是: {encryption: xxx} 其中encryption就是key xxx就是加密后的请求体参数 {"username":
	 * "admin", "password": "123456"}
	 */
	private String encryptionKey = "encryption";

	/**
	 * 服务端加解密使用的密钥 对于非对称加密来说，应该是私钥。
	 */
	private String key = "qwertyuioplkjhnm";

	/**
	 * 加密类型
	 */
	private EncryptType encryptType = EncryptType.AES;

}
