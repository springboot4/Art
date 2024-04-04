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

package com.art.api.encrypt.sdk.core;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.symmetric.AES;
import com.art.api.encrypt.sdk.constants.EncryptType;
import org.springframework.util.StringUtils;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 接口加密转换器
 *
 * @author fxz
 * @version 0.0.1
 * @date 2024/4/4 15:46
 */
public interface ApiEncryptConvert {

	default String encryptData(byte[] jsonData, EncryptType type, String key) {
		if (type == null) {
			throw new UnsupportedOperationException("Unsupported encryption type");
		}
		if (!StringUtils.hasText(key)) {
			throw new UnsupportedOperationException("Encryption key is not configured");
		}

		if (type == EncryptType.DES) {
			return SecureUtil.des(key.getBytes(StandardCharsets.UTF_8)).encryptBase64(jsonData);
		}
		if (type == EncryptType.AES) {
			AES aes = new AES(Mode.CFB, Padding.NoPadding, new SecretKeySpec(key.getBytes(), "AES"),
					new IvParameterSpec(key.getBytes()));
			return aes.encryptBase64(jsonData);
		}
		if (type == EncryptType.RSA) {
			return SecureUtil.rsa(key.getBytes(StandardCharsets.UTF_8), null)
				.encryptBase64(jsonData, KeyType.PrivateKey);
		}

		throw new UnsupportedOperationException("Unsupported encryption type");
	}

}
