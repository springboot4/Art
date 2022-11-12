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

package com.fxz.common.canal.support.parser;

import com.fxz.common.canal.annotation.CanalField;
import com.fxz.common.canal.annotation.CanalModel;
import com.fxz.common.canal.common.NamingPolicy;
import com.fxz.common.canal.model.ModelTable;
import com.fxz.common.canal.support.parser.converter.BaseCanalFieldConverter;
import com.fxz.common.canal.support.parser.converter.CanalFieldConvertInput;
import com.fxz.common.canal.support.parser.converter.CanalFieldConvertResult;
import com.fxz.common.canal.support.parser.converter.CanalFieldConverterFactory;
import com.fxz.common.canal.util.AssertUtils;
import com.fxz.common.canal.util.ReflectionUtils;
import com.fxz.common.canal.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author fxz
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public class InMemoryModelTableMetadataManager implements ModelTableMetadataManager {

	private final ConcurrentMap<Class<?>, ModelTableMetadata> cache = new ConcurrentHashMap<>(16);

	private final CanalFieldConverterFactory canalFieldConverterFactory;

	@Override
	public ModelTableMetadata load(Class<?> klass) {
		return cache.computeIfAbsent(klass, clazz -> {
			AssertUtils.X.isTrue(klass.isAnnotationPresent(CanalModel.class),
					String.format("[%s]没有使用@CanalModel注解", klass.getName()));
			CanalModel canalModel = klass.getAnnotation(CanalModel.class);
			NamingPolicy namingPolicy = canalModel.fieldNamingPolicy();

			ModelTableMetadata metadata = new ModelTableMetadata();
			metadata.setModelTable(ModelTable.of(canalModel.database(), canalModel.table()));
			Map<String, ColumnMetadata> fieldColumnMapping = new HashMap<>(8);
			ReflectionUtils.X.doWithFields(klass, field -> {
				CanalField canalField;
				JDBCType sqlType = null;
				Class<? extends BaseCanalFieldConverter<?>> converterKlass = null;
				String columnName = null;
				if (field.isAnnotationPresent(CanalField.class)) {
					canalField = field.getAnnotation(CanalField.class);
					sqlType = canalField.sqlType();
					converterKlass = canalField.converterKlass();
					if (StringUtils.X.isNotEmpty(canalField.columnName())) {
						columnName = canalField.columnName();
					}
				}
				String fieldName = field.getName();
				if (null == columnName) {
					columnName = namingPolicy.convert(fieldName);
				}
				CanalFieldConvertInput input = CanalFieldConvertInput.builder().fieldKlass(field.getType())
						.sqlType(sqlType).converterKlass(converterKlass).build();
				CanalFieldConvertResult result = canalFieldConverterFactory.load(input);
				ColumnMetadata columnMetadata = new ColumnMetadata();
				columnMetadata.setColumnName(columnName);
				columnMetadata.setConverter(result.getConverter());
				fieldColumnMapping.put(fieldName, columnMetadata);
			});
			metadata.setFieldColumnMapping(fieldColumnMapping);
			return metadata;
		});
	}

}
