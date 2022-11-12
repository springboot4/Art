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

package com.fxz.common.mp.encrypt.propertie;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/15 14:05
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "fxz.common.encrypt")
public class EncryptProperties {

	/**
	 * 是否开启字段加密
	 */
	private boolean enableFieldDecrypt = true;

	/**
	 * aes秘钥
	 */
	private String fieldDecryptKey = "0123456789ABHAEQ";

}
