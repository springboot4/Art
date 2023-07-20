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

package com.art.mybatis.sdk.func;

import com.art.mybatis.sdk.annotation.EncryptionData;
import com.art.mybatis.sdk.base.EncryptExecutor;
import com.art.mybatis.sdk.base.PojoCloneable;
import com.art.mybatis.sdk.enums.TypeEnum;
import com.art.mybatis.sdk.info.BeanEncryptDetailInfo;
import com.art.mybatis.sdk.support.EncryptInfoContext;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 解密相关方法
 *
 * @author fxz
 */
@SuppressWarnings("unchecked")
@Slf4j
public class DecryptFunc {

	private final EncryptExecutor encryptExecutor;

	public DecryptFunc(EncryptExecutor encryptExecutor) {
		this.encryptExecutor = encryptExecutor;
	}

	/**
	 * 解密
	 * @param rowResult sql查询出来的原生结果
	 * @param encryptInfoContext 对应的解密信息
	 * @return 解密后的结果
	 */
	public Object doDecrypt(Object rowResult, EncryptInfoContext encryptInfoContext) {
		if (rowResult == null) {
			return null;
		}

		TypeEnum typeEnum = TypeEnum.parseType(rowResult.getClass());
		BeanEncryptDetailInfo decryptBeanInfo = encryptInfoContext.getDecryptBeanInfo();
		Object newResult;
		String rowResultStr = null;
		if (log.isTraceEnabled()) {
			rowResultStr = rowResult.toString();
		}

		switch (typeEnum) {
			case PRIMITIVE_OR_WRAPPER:
			case STRING:
			case SYSTEM_BEAN:
				return rowResult;
			case MAP:
				newResult = mapDecrypt((Map<String, Object>) rowResult, decryptBeanInfo);
				break;
			case COLLECTION:
				newResult = collectionDecrypt((Collection<Object>) rowResult, decryptBeanInfo);
				break;
			case ARRAY:
				newResult = arrayDecrypt((Object[]) rowResult, decryptBeanInfo);
				break;
			case CUSTOM_BEAN:
				newResult = pojoDecrypt(rowResult, decryptBeanInfo);
				break;
			default:
				throw new IllegalArgumentException("Cannot support for typeEnum [" + typeEnum + "]");
		}

		if (log.isTraceEnabled()) {
			log.trace("doDecrypt from [{}] to [{}]", rowResultStr, newResult);
		}
		return newResult;
	}

	/**
	 * string解密
	 * @param strName 字段名或@Param指定的名称
	 * @param strValue 待解密的字段值
	 * @param annotation 解密注解信息
	 * @param obj 字段所在的当前对象
	 * @return 解密后的字符串
	 */
	private String stringDecrypt(String strName, String strValue, EncryptionData annotation, Object obj) {
		if (StringUtils.isEmpty(strValue) || Objects.isNull(obj)) {
			return strValue;
		}

		return encryptExecutor.decryptField(strName, strValue, annotation, obj);
	}

	/**
	 * 对象类型解密
	 */
	private <T> T pojoDecrypt(T originPojo, BeanEncryptDetailInfo decryptBeanInfo) {
		if (Objects.isNull(originPojo)) {
			return originPojo;
		}

		Class<?> originPojoClass = originPojo.getClass();
		TypeEnum typeEnum = TypeEnum.parseType(originPojoClass);
		// 基础类型直接返回
		if (typeEnum == TypeEnum.PRIMITIVE_OR_WRAPPER) {
			return originPojo;
		}
		if (typeEnum != TypeEnum.CUSTOM_BEAN) {
			throw new IllegalStateException(originPojoClass.getName() + " is not custom bean.");
		}

		final T returnPojo;
		if (originPojo instanceof PojoCloneable) {
			returnPojo = (T) ((PojoCloneable) originPojo).clonePojo();
		}
		else {
			returnPojo = originPojo;
		}

		Map<Field, EncryptionData> fieldEncryptMap = decryptBeanInfo.getFieldEncryptMap();
		fieldEncryptMap.forEach(((field, encryptionData) -> {
			Object oldValue = null;
			try {
				oldValue = FieldUtils.readField(field, returnPojo, true);
				if (Objects.isNull(oldValue)) {
					return;
				}

				String oldValueStr = oldValue.toString();
				if (StringUtils.isEmpty(oldValueStr)) {
					return;
				}

				FieldUtils.writeField(field, returnPojo,
						stringDecrypt(field.getName(), oldValueStr, encryptionData, returnPojo), true);
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException(
						String.format("handle field [%s] occur exception. oldValue is %s", field, oldValue), e);
			}
		}));
		return returnPojo;
	}

	/**
	 * map解密
	 */
	private Map<String, Object> mapDecrypt(Map<String, Object> map, BeanEncryptDetailInfo decryptBeanInfo) {
		if (CollectionUtils.isEmpty(map)) {
			return map;
		}

		map.forEach((k, v) -> {
			Object newObj = pojoDecrypt(v, decryptBeanInfo);
			map.put(k, newObj);
		});

		return map;
	}

	/**
	 * 集合解密
	 */
	private Collection<Object> collectionDecrypt(Collection<Object> collection, BeanEncryptDetailInfo decryptBeanInfo) {
		if (CollectionUtils.isEmpty(collection)) {
			return collection;
		}

		ArrayList<Object> list = new ArrayList<>(collection.size());
		collection.forEach(pojo -> list.add(pojoDecrypt(pojo, decryptBeanInfo)));

		return list;
	}

	/**
	 * 数组解密
	 */
	private <T> T[] arrayDecrypt(T[] array, BeanEncryptDetailInfo decryptBeanInfo) {
		if (array.length == 0) {
			return array;
		}

		for (int i = 0; i < array.length; i++) {
			array[i] = pojoDecrypt(array[i], decryptBeanInfo);
		}

		return array;
	}

}