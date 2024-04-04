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

import com.art.mybatis.sdk.annotation.EncryptionData;

/**
 * 加解密执行器
 *
 * @author fxz
 */
public interface EncryptExecutor {

	/**
	 * 加密
	 * @param paramName 参数前@Param指定的名称
	 * @param paramValue 待加密的字段值
	 * @param annotation 加密注解信息
	 * @return 加密后的数据
	 */
	String encryptParameter(String paramName, String paramValue, EncryptionData annotation);

	/**
	 * 加密
	 * @param fieldName 待加密的字段的字段名
	 * @param fieldValue 待加密的字段的字段值
	 * @param annotation 加密注解信息
	 * @param pojo 字段所在的当前对象
	 * @return 加密后的数据
	 */
	String encryptField(String fieldName, String fieldValue, EncryptionData annotation, Object pojo);

	/**
	 * 解密
	 * @param fieldName 待解密字段的字段名称
	 * @param fieldValue 待解密字段的值
	 * @param annotation 解密注解信息
	 * @param pojo fieldValue对应所处的对象
	 * @return 解密后的数据
	 */
	String decryptField(String fieldName, String fieldValue, EncryptionData annotation, Object pojo);

}
