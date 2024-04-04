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

package com.art.mybatis.sdk.info;

import com.art.mybatis.sdk.annotation.EncryptionData;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.*;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * bean的加解密信息
 *
 * @author fxz
 */
@ToString
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanEncryptDetailInfo {

	/**
	 * 字段和对应的加解密信息
	 */
	private Map<Field, EncryptionData> fieldEncryptMap;

	/**
	 * 列名、字段、加密信息
	 */
	private Map<String, Pair<Field, EncryptionData>> columnEncryptMap;

	/**
	 * 当前BeanEncryptDetailInfo对象所执行的bean的类型
	 */
	private Class<?> beanClass;

	/**
	 * 创建BeanEncryptDetailInfo实例
	 * @param beanClass 待解析的class
	 * @return BeanEncryptDetailInfo实例
	 */
	@NonNull
	public static BeanEncryptDetailInfo build(Class<?> beanClass) {
		BeanEncryptDetailInfo beanEncryptDetailInfo = new BeanEncryptDetailInfo();
		beanEncryptDetailInfo.setBeanClass(beanClass);
		beanEncryptDetailInfo.setFieldEncryptMap(extractBeanFieldEncryptInfo(beanClass));
		beanEncryptDetailInfo
			.setColumnEncryptMap(extractTableColumnEncryptInfo(beanEncryptDetailInfo.getFieldEncryptMap()));
		return beanEncryptDetailInfo;
	}

	/**
	 * 提取类中的加密注解
	 * @param beanClass 待解析的class
	 * @return class中的 字段和加密信息map
	 */
	public static Map<Field, EncryptionData> extractBeanFieldEncryptInfo(Class<?> beanClass) {
		Map<Field, EncryptionData> infoMap = new HashMap<>(8);
		ReflectionUtils.doWithFields(beanClass, (Field field) -> {
			EncryptionData annotation = field.getAnnotation(EncryptionData.class);
			if (Objects.nonNull(annotation)) {
				Class<?> fieldType = field.getType();
				if (TypeUtils.isAssignable(fieldType, String.class)) {
					infoMap.put(field, annotation);
				}
				else {
					throw new IllegalArgumentException(String.format(
							"@EncryptionData is not allowed to apply at type [%s], only for String,class. @see %s",
							fieldType, beanClass.getName()));
				}
			}
		});

		return infoMap;
	}

	/**
	 * 提取数据库表的列名以及对应的加密信息
	 * @param fieldEncryptMap 字段及加密信息
	 * @return 列名以及对应的加密信息
	 */
	public static Map<String, Pair<Field, EncryptionData>> extractTableColumnEncryptInfo(
			Map<Field, EncryptionData> fieldEncryptMap) {
		LinkedHashMap<String, Pair<Field, EncryptionData>> columnEncryptMap = new LinkedHashMap<>();

		for (Map.Entry<Field, EncryptionData> entry : fieldEncryptMap.entrySet()) {
			String columnName = null;

			Field field = entry.getKey();
			TableField tableFieldAnno = field.getAnnotation(TableField.class);
			if (tableFieldAnno != null && tableFieldAnno.exist()) {
				columnName = tableFieldAnno.value();
			}
			if (StringUtils.isBlank(columnName)) {
				columnName = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(field.getName());
			}
			Objects.requireNonNull(columnName, "columnName is null.");

			columnName = columnName.replace("`", "");
			columnEncryptMap.put(columnName, Pair.of(field, entry.getValue()));
		}
		return columnEncryptMap;
	}

}