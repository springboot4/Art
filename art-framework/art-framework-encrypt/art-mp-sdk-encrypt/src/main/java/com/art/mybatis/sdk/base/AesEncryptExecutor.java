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

package com.art.mybatis.sdk.base;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.art.mybatis.sdk.annotation.EncryptionData;
import com.art.mybatis.sdk.propertie.EncryptProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

/**
 * Aes加密执行器
 *
 * @author fxz
 */
@Getter
@RequiredArgsConstructor
public class AesEncryptExecutor implements EncryptExecutor {

	private final EncryptProperties encryptProperties;

	@Override
	public String encryptParameter(String paramName, String paramValue, EncryptionData annotation) {
		if (!encryptProperties.isEnableFieldDecrypt() || !encryptProperties.isEncrypt()) {
			return paramValue;
		}

		AES aes = SecureUtil.aes(encryptProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
		return aes.encryptBase64(paramValue);
	}

	@Override
	public String encryptField(String fieldName, String fieldValue, EncryptionData annotation, Object pojo) {
		if (!encryptProperties.isEnableFieldDecrypt() || !encryptProperties.isEncrypt()) {
			return fieldValue;
		}

		AES aes = SecureUtil.aes(encryptProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
		return aes.encryptBase64(fieldValue);
	}

	@Override
	public String decryptField(String fieldName, String fieldValue, EncryptionData annotation, Object pojo) {
		if (!encryptProperties.isEnableFieldDecrypt() || !encryptProperties.isDecrypt()) {
			return fieldValue;
		}

		AES aes = SecureUtil.aes(encryptProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
		return new String(aes.decrypt(Base64.decode(fieldValue)), StandardCharsets.UTF_8);
	}

}
