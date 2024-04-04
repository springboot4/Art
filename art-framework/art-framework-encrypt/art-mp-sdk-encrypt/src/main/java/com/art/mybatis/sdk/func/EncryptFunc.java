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
import com.art.mybatis.sdk.info.ParamEncryptDetailInfo;
import com.art.mybatis.sdk.support.EncryptInfoContext;
import com.art.mybatis.sdk.util.MybatisPlusSupportUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加密相关方法
 *
 * @author fxz
 */
public class EncryptFunc {

	/**
	 * key是MappedStatement的id,value是mybatis-plus动态生成的查询当前（MappedStatement）前作count的MappedStatement
	 * id
	 */
	private final Map<String, String> statementIdAndMpCountStatementIdCache = new ConcurrentHashMap<>(128);

	private final EncryptExecutor encryptExecutor;

	public EncryptFunc(EncryptExecutor encryptExecutor) {
		this.encryptExecutor = encryptExecutor;
	}

	/**
	 * 当前mappedStatement查询，是否存在mybatis-plus自动生成的count查询
	 * @param mappedStatementId sql对应的MappedStatement的id
	 * @return mappedStatementId对应的加解密信息详情类
	 */
	public boolean existMpCount(String mappedStatementId) {
		return statementIdAndMpCountStatementIdCache.containsKey(mappedStatementId);
	}

	/**
	 * 添加当前mappedStatement查询对应的mybatis-plus自动生成的count查询标识
	 * @param mappedStatementId sql对应的MappedStatement的id
	 * @param mpCountMappedStatementId mybatis-plus自动生成的count查询的MappedStatement的id
	 */
	public void putMpCount(String mappedStatementId, String mpCountMappedStatementId) {
		statementIdAndMpCountStatementIdCache.put(mappedStatementId, mpCountMappedStatementId);
	}

	/**
	 * 加密
	 * @param mappedStatement 当前sql对应的mappedStatement对象
	 * @param parameter
	 * 到mybatis插件时的参数。即：{@link Executor#update(MappedStatement, Object)}的第二个参数
	 * @param encryptInfoContext 对应的加密信息上下文
	 * @return 加密后的parameter
	 */
	@SuppressWarnings("unchecked")
	public Object doEncrypt(MappedStatement mappedStatement, Object parameter, EncryptInfoContext encryptInfoContext) {
		// 参数为null 不处理
		if (parameter == null) {
			return null;
		}

		// 参数类型
		TypeEnum typeEnum = TypeEnum.parseType(parameter.getClass());

		if (typeEnum == TypeEnum.PRIMITIVE_OR_WRAPPER || typeEnum == TypeEnum.STRING) {
			// 加密注解使用在方法入参时 已强制要求同时使用@Param注解指定名称 此时的parameter类型是Map而非String
			return parameter;
		}

		// 对应的bean加密信息
		List<BeanEncryptDetailInfo> encryptBeanInfoList = encryptInfoContext.getEncryptBeanInfoList();
		// 对应的参数类型加密信息
		ParamEncryptDetailInfo encryptParamInfo = encryptInfoContext.getEncryptParamInfo();
		// 处理后的参数信息
		Object newParameter;

		switch (typeEnum) {
			case MAP:
				// Map类型加密
				newParameter = mapEncrypt(mappedStatement, (Map<String, Object>) parameter, encryptBeanInfoList,
						encryptParamInfo);
				break;
			case COLLECTION:
				// Collection类型加密
				newParameter = collectionEncrypt(mappedStatement, (Collection<Object>) parameter, encryptBeanInfoList);
				break;
			case ARRAY:
				// 数组类型加密
				newParameter = arrayEncrypt(mappedStatement, (Object[]) parameter, encryptBeanInfoList);
				break;
			case CUSTOM_BEAN:
				// 自定义的java bean类型加密
				newParameter = pojoEncrypt(mappedStatement, parameter, encryptBeanInfoList);
				break;
			default:
				newParameter = parameter;
		}

		return newParameter;
	}

	/**
	 * string加密
	 * @param strName 字段名 注:当加密注解应用于方法入参上时 此为@Param指定的名称
	 * @param strValue 待加密的字段值
	 * @param annotation 加密注解信息
	 * @param obj 字段所在的当前对象 当方法入参上时obj为null
	 * @return 加密后的字符串
	 */
	public String stringEncrypt(String strName, String strValue, EncryptionData annotation, Object obj) {
		if (StringUtils.isEmpty(strValue)) {
			return strValue;
		}

		return Objects.isNull(obj) ? encryptExecutor.encryptParameter(strName, strValue, annotation)
				: encryptExecutor.encryptField(strName, strValue, annotation, obj);
	}

	/**
	 * 对象类型加密
	 */
	@SuppressWarnings("unchecked,rawtypes")
	public <T> T pojoEncrypt(MappedStatement mappedStatement, T originPojo,
			List<BeanEncryptDetailInfo> encryptBeanInfoList) {
		if (Objects.isNull(originPojo)) {
			return null;
		}

		// 分页count查询
		if (statementIdAndMpCountStatementIdCache.containsKey(mappedStatement.getId())) {
			return originPojo;
		}

		// 如果是Wrapper
		if (MybatisPlusSupportUtil.isWrapperSubclass(originPojo)) {
			// 对Wrapper进行加密
			return (T) MybatisPlusSupportUtil.encryptWrapper(mappedStatement, (Wrapper) originPojo, encryptBeanInfoList,
					this);
		}

		T returnPojo;
		if (originPojo instanceof PojoCloneable) {
			returnPojo = (T) ((PojoCloneable) originPojo).clonePojo();
		}
		else {
			returnPojo = originPojo;
		}

		Object encryptPojo = returnPojo;

		// 获取bean对应的加密信息
		BeanEncryptDetailInfo beanEncryptDetailInfo = encryptBeanInfoList.stream()
			.filter(x -> x.getBeanClass().isAssignableFrom(encryptPojo.getClass()))
			.findFirst()
			.orElse(null);

		if (Objects.isNull(beanEncryptDetailInfo)) {
			return returnPojo;
		}

		// bean字段需要加密的信息
		Map<Field, EncryptionData> fieldEncryptMap = beanEncryptDetailInfo.getFieldEncryptMap();
		// 遍历bean需要加密的字段 进行String加密
		fieldEncryptMap.forEach(((field, encryptionData) -> {
			Object oldValue = null;
			try {
				oldValue = FieldUtils.readField(field, encryptPojo, true);
				if (Objects.isNull(oldValue)) {
					return;
				}

				String oldValueStr = oldValue.toString();
				if (StringUtils.isEmpty(oldValueStr)) {
					return;
				}

				FieldUtils.writeField(field, encryptPojo,
						stringEncrypt(field.getName(), oldValueStr, encryptionData, encryptPojo), true);
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException(
						String.format("handle field [%s] occur exception. oldValue is %s", field, oldValue), e);
			}
		}));

		return returnPojo;
	}

	/**
	 * map加密
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> mapEncrypt(MappedStatement mappedStatement, Map<String, Object> map,
			List<BeanEncryptDetailInfo> encryptBeanInfoList, ParamEncryptDetailInfo encryptParamInfo) {
		if (CollectionUtils.isEmpty(map)) {
			return map;
		}

		Map<String, EncryptionData> paramEncryptMap = encryptParamInfo.getParamEncryptMap();
		MapperMethod.ParamMap<Object> paramMap = new MapperMethod.ParamMap<>();
		paramMap.putAll(map);

		String key;
		Object value;
		boolean valueIsString;
		// 避免同一个map中的value重复加密
		final Set<Object> alreadyEncryptedSet = new HashSet<>(8);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			// value 是null 或者 是基础类型获其包装类
			if (value == null || ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
				continue;
			}

			if (alreadyEncryptedSet.contains(value)) {
				continue;
			}

			// value是字符串
			valueIsString = value instanceof String;
			if (valueIsString) {
				if (paramEncryptMap.containsKey(key)) {
					paramMap.put(key, stringEncrypt(key, (String) value, paramEncryptMap.get(key), null));
				}
				continue;
			}

			// value是map
			if (value instanceof Map) {
				// 只有第一层Map待会有@Param的信息，后面层的map是不会有@Param的，为了避免误加密里层map，所以这里传一个EMPTY进去即可
				value = mapEncrypt(mappedStatement, (Map<String, Object>) value, encryptBeanInfoList,
						ParamEncryptDetailInfo.EMPTY);
				paramMap.put(key, value);
				alreadyEncryptedSet.add(value);
				continue;
			}

			// value是collection
			if (value instanceof Collection) {
				value = collectionEncrypt(mappedStatement, (Collection<Object>) value, encryptBeanInfoList);
				paramMap.put(key, value);
				alreadyEncryptedSet.add(value);
				continue;
			}

			// value是array
			if (value instanceof Object[]) {
				Object newValue = arrayEncrypt(mappedStatement, (Object[]) value, encryptBeanInfoList);
				paramMap.put(key, newValue);
				alreadyEncryptedSet.add(value);
				continue;
			}

			// value是Enum，那么不处理
			if (value instanceof Enum) {
				continue;
			}

			// 上述情况都不成立，那么value是普通pojo
			value = pojoEncrypt(mappedStatement, value, encryptBeanInfoList);
			paramMap.put(key, value);
			alreadyEncryptedSet.add(value);
		}
		alreadyEncryptedSet.clear();
		return paramMap;
	}

	/**
	 * 集合加密
	 */
	private Collection<Object> collectionEncrypt(MappedStatement mappedStatement, Collection<Object> collection,
			List<BeanEncryptDetailInfo> encryptBeanInfoList) {
		ArrayList<Object> list = new ArrayList<>(collection.size());
		if (CollectionUtils.isEmpty(collection)) {
			return list;
		}
		// 统一泛型，一般的，第一个子元素的类型就能代表所有子元素的类型
		Object subElement = collection.iterator().next();
		Objects.requireNonNull(subElement, "exist null element in collection " + collection);
		TypeEnum typeEnum = TypeEnum.parseType(subElement.getClass());
		if (typeEnum != TypeEnum.CUSTOM_BEAN) {
			return collection;
		}
		// 只处理普通业务bean
		collection.forEach(pojo -> list.add(pojoEncrypt(mappedStatement, pojo, encryptBeanInfoList)));
		return list;
	}

	/**
	 * 数组加密
	 */
	private <T> T[] arrayEncrypt(MappedStatement mappedStatement, @NonNull T[] array,
			List<BeanEncryptDetailInfo> encryptBeanInfoList) {
		if (array.length == 0) {
			return array;
		}
		// 统一泛型，一般的，第一个子元素的类型就能代表所有子元素的类型
		Object subElement = array[0];
		Objects.requireNonNull(subElement, "exist null element in array " + Arrays.toString(array));
		TypeEnum typeEnum = TypeEnum.parseType(subElement.getClass());
		if (typeEnum != TypeEnum.CUSTOM_BEAN) {
			return array;
		}
		// 只处理普通业务bean
		for (int i = 0; i < array.length; i++) {
			array[i] = pojoEncrypt(mappedStatement, array[i], encryptBeanInfoList);
		}
		return array;
	}

}