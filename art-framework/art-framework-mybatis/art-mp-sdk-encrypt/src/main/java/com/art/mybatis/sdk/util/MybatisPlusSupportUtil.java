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

package com.art.mybatis.sdk.util;

import com.art.mybatis.sdk.annotation.EncryptionData;
import com.art.mybatis.sdk.base.PojoCloneable;
import com.art.mybatis.sdk.func.EncryptFunc;
import com.art.mybatis.sdk.info.BeanEncryptDetailInfo;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import sun.reflect.generics.repository.ClassRepository;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author fxz
 */
public final class MybatisPlusSupportUtil {

	/**
	 * 泛型左符号
	 */
	private static final String GENERIC_LEFT_SIGN = StringPool.LEFT_CHEV;

	/**
	 * 泛型右符号
	 */
	private static final String GENERIC_RIGHT_SIGN = StringPool.RIGHT_CHEV;

	/**
	 * java.lang.Class#getGenericInfo()
	 */
	private static final Method GET_GENERIC_INFO;

	/**
	 * java.lang.reflect.Method#getGenericSignature()
	 */
	private static final Method GET_GENERIC_SIGNATURE;

	/**
	 * com.baomidou.mybatisplus.core.conditions.AbstractWrapper#entity
	 */
	private static final Field ENTITY;

	static {
		GET_GENERIC_SIGNATURE = ReflectionUtils.findMethod(Method.class, "getGenericSignature");
		Objects.requireNonNull(GET_GENERIC_SIGNATURE).setAccessible(true);

		GET_GENERIC_INFO = ReflectionUtils.findMethod(Class.class, "getGenericInfo");
		Objects.requireNonNull(GET_GENERIC_INFO).setAccessible(true);

		ENTITY = ReflectionUtils.findField(AbstractWrapper.class, "entity");
		Objects.requireNonNull(ENTITY).setAccessible(true);
	}

	/**
	 * clazz是否是mybatis-plus对应的数据库表对应的数据模型
	 * @param clazz 待判断的clazz
	 * @return clazz是否是mybatis-plus对应的数据库表对应的数据模型
	 */
	public static boolean isDbEntity(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}

		List<Annotation> list = new ArrayList<>(16);
		while (clazz != Object.class) {
			list.addAll(Arrays.asList(clazz.getAnnotations()));
			clazz = clazz.getSuperclass();
		}

		return list.stream().anyMatch(anno -> Objects.equals(anno.annotationType(), TableName.class));
	}

	/**
	 * 加密warrapper<T>中相关的数据
	 * @param mappedStatement sql对应的mappedStatement实例
	 * @param wrapper 对象（可能包装有DbEntity相关数据）
	 * @param encryptBeanInfoList 所有需要加的类的信息
	 * @param encryptFunc 加密器
	 * @return warrapper<T>对象
	 */
	@SuppressWarnings("rawtypes")
	public static Wrapper encryptWrapper(MappedStatement mappedStatement, @SuppressWarnings("rawtypes") Wrapper wrapper,
			List<BeanEncryptDetailInfo> encryptBeanInfoList, EncryptFunc encryptFunc) {
		// mapper对应的数据库实体
		Class<?> dbEntityClass = MybatisPlusSupportUtil.parseMybatisPlusDbEntityClass(mappedStatement);

		// 数据库实体对应的加密信息
		BeanEncryptDetailInfo beanEncryptDetailInfo = encryptBeanInfoList.stream()
			.filter(x -> x.getBeanClass() == dbEntityClass)
			.findFirst()
			.orElse(null);
		if (Objects.isNull(beanEncryptDetailInfo)) {
			return wrapper;
		}

		// 列名与数据库实体对应加密信息
		Map<String, Pair<Field, EncryptionData>> columnEncryptMap = beanEncryptDetailInfo.getColumnEncryptMap();

		// wrapper里需要加密
		AbstractWrapper abstractWrapper = assertAbstractMapper(wrapper);

		// entity需要加密
		Object entity = wrapper.getEntity();
		if (entity instanceof PojoCloneable) {
			entity = encryptFunc.pojoEncrypt(mappedStatement, entity, encryptBeanInfoList);
			try {
				// 反射设置克隆后的对象
				ENTITY.set(abstractWrapper, entity);
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException("invoke java.lang.reflect.Field.set error.", e);
			}
		}
		else {
			encryptFunc.pojoEncrypt(mappedStatement, entity, encryptBeanInfoList);
		}

		if (abstractWrapper == null) {
			return wrapper;
		}

		// 加载sql 使得所有的参数(set参数 & where后的条件参数)都放能出现在paramNameValuePairs
		wrapper.getSqlSegment();

		// MPGENVAL1:张三
		@SuppressWarnings("unchecked")
		Map<String, Object> paramNameValuePairs = abstractWrapper.getParamNameValuePairs();
		if (CollectionUtils.isEmpty(paramNameValuePairs)) {
			return wrapper;
		}

		/*
		 * 记录paramNameValuePairs中需要加密的项 MPGENVAL1:name
		 */
		Map<String, String> paramNameValuePairsNeedEncryptKey = new LinkedHashMap<>();
		AtomicInteger idx = new AtomicInteger(0);

		// SQL更新字段内容
		// 形如：name=#{ew.paramNameValuePairs.MPGENVAL1},gender=#{ew.paramNameValuePairs.MPGENVAL2}
		// 出现于update中 例如: .set(User::getName, "张三").set(User::getGender, "男")
		String sqlSet = wrapper.getSqlSet();
		if (StringUtils.isNotBlank(sqlSet)) {
			Arrays.stream(
					// 分割后每一个元素的效果 形如：name=#{ew.paramNameValuePairs.MPGENVAL1}
					sqlSet.split(StringPool.COMMA))
				.filter(StringUtils::isNotBlank)
				.forEach(str -> {
					// 列名
					String columnName = str.split(StringPool.EQUALS)[0].trim();
					// 如果该字段需要加密
					if (columnEncryptMap.containsKey(columnName)) {
						// 保存需要加密的参数名和字段 形如：MPGENVAL1:name
						paramNameValuePairsNeedEncryptKey.put(Constants.WRAPPER_PARAM + idx.incrementAndGet(),
								columnName);
					}
					else {
						idx.incrementAndGet();
					}
				});
		}

		// where部分
		MergeSegments expression = wrapper.getExpression();
		if (expression != null) {
			// 处理normal while条件
			NormalSegmentList normal = expression.getNormal();
			int size = normal.size();
			for (int i = 0; i < size; i++) {
				boolean currSegmentIsColumnName = i + 1 < size && normal.get(i + 1) instanceof SqlKeyword;
				if (currSegmentIsColumnName) {
					// 列名 形如：name
					String columnName = normal.get(i).getSqlSegment();
					columnName = columnName.replace(StringPool.BACKTICK, StringPool.EMPTY);
					boolean existNeedEncryptColumn = columnEncryptMap.containsKey(columnName);
					boolean existValueSegment = i + 2 < size;
					if (existNeedEncryptColumn && existValueSegment) {
						String sqlSegment = normal.get(i + 2).getSqlSegment();
						// 类似于in， 一个sql片段中可能会有多个占位符。 如:
						// (#{ew.paramNameValuePairs.MPGENVAL1},#{ew.paramNameValuePairs.MPGENVAL2})
						int count = StringUtils.countMatches(sqlSegment, Constants.WRAPPER_PARAM);
						for (int j = 0; j < count; j++) {
							// 保存需要加密的参数名和字段 形如：MPGENVAL1:name
							paramNameValuePairsNeedEncryptKey.put(Constants.WRAPPER_PARAM + idx.incrementAndGet(),
									columnName);
						}
					}
					else {
						idx.incrementAndGet();
					}
				}
			}
		}

		Map<String, Object> tmpMap = new HashMap<>(16);
		for (Map.Entry<String, Object> entry : paramNameValuePairs.entrySet()) {
			// 参数名 形如：MPGENVAL3
			String key = entry.getKey();
			// 字段值 形如：张三
			Object value = entry.getValue();

			// 当值为String类型且当前参数需要加密时
			if (value instanceof String && paramNameValuePairsNeedEncryptKey.containsKey(key)) {
				// 获取参数对应的列名
				String columnName = paramNameValuePairsNeedEncryptKey.get(key);
				// 根据列名获取字段加密信息
				Pair<Field, EncryptionData> fieldEncrypt = columnEncryptMap.get(columnName);
				Objects.requireNonNull(fieldEncrypt, "fieldEncrypt cannot be null.");

				Field field = fieldEncrypt.getKey();
				// 加密并替换
				value = encryptFunc.stringEncrypt(field.getName(), value.toString(), fieldEncrypt.getValue(), wrapper);
			}

			tmpMap.put(key, value);
		}

		// 替换为密文的
		paramNameValuePairs.putAll(tmpMap);

		return wrapper;
	}

	/**
	 * 筛选出mappedStatement对应的方法 主要是解决mp方法重载
	 * com.baomidou.mybatisplus.core.mapper.BaseMapper.deleteById(java.io.Serializable)
	 * com.baomidou.mybatisplus.core.mapper.BaseMapper.deleteById(T)
	 * @param mappedStatement sql的MappedStatement实例
	 * @param targetMethodList
	 * 根据mappedStatement。getId()里面的类名和方法名匹配出来的Method,但因为可能重载，所以可能有多个
	 */
	public static List<Method> filterMethod(MappedStatement mappedStatement, List<Method> targetMethodList) {
		List<Method> methods = new ArrayList<>(targetMethodList);
		if (methods.size() <= 1) {
			return methods;
		}

		Class<?> parameterType = mappedStatement.getParameterMap().getType();
		boolean isDbEntity = isDbEntity(parameterType);
		Class<?> targetParamClass = isDbEntity ? Object.class : Serializable.class;
		return methods.stream().filter(m -> {
			for (Class<?> paramClass : m.getParameterTypes()) {
				if (targetParamClass == paramClass) {
					return true;
				}
			}
			return false;
		}).collect(Collectors.toList());
	}

	/**
	 * 是否是BaseMapper中内置的方法
	 * @param method method
	 * @return 是否是BaseMapper中内置的方法
	 */
	public static boolean isMybatisPlusMethod(Method method) {
		return Objects.equals(method.getDeclaringClass(), BaseMapper.class);
	}

	/**
	 * 方法的参数是否包含要加密的T <br />
	 * 主要用于识别参数中是否有： Wrapper<T> 或者 T
	 * @param method 目标方法
	 * @return 参数否是包含T
	 */
	public static boolean parameterContainDbEntity(Method method) {
		for (Parameter parameter : method.getParameters()) {
			Class<?> parameterType = parameter.getType();
			if (Wrapper.class.equals(parameterType) || Object.class.equals(parameterType)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断object是否是Wrapper类 <br />
	 * @param object 判断的对象
	 * @return object是否是Wrapper类
	 */
	public static boolean isWrapperSubclass(Object object) {
		return object instanceof Wrapper;
	}

	/**
	 * 方法的返回类型中是否包含T <br/>
	 * 样例：
	 * @param method 返回值类型
	 * @return 返回值中是否包含T
	 */
	public static boolean returnClassContainDbEntity(Method method) {
		// 方法签名信息
		Object getGenericSignature = ReflectionUtils.invokeMethod(GET_GENERIC_SIGNATURE, method);
		if (getGenericSignature == null) {
			return false;
		}

		String signature = getGenericSignature.toString();

		if (signature.contains("IPage<TT;>")) {
			return true;
		}

		String returnObjSignature = signature.substring(signature.lastIndexOf(")") + 1);
		if (returnObjSignature.contains("<")) {
			int startIndex = returnObjSignature.indexOf(GENERIC_LEFT_SIGN);
			int endIndex = returnObjSignature.lastIndexOf(GENERIC_RIGHT_SIGN);
			if (startIndex == -1 || endIndex == -1) {
				return false;
			}
			String genericInfo = returnObjSignature.substring(startIndex + 1, endIndex);
			return genericInfo.contains("TT");
		}
		else {
			return returnObjSignature.contains("TT");
		}
	}

	/**
	 * 解析mappedStatement对应的class
	 * @param mappedStatement sql的MappedStatement实例
	 * @return mappedStatement对应的数据模型的class
	 */
	public static Class<?> parseMybatisPlusDbEntityClass(MappedStatement mappedStatement) {
		try {
			String mappedStatementId = mappedStatement.getId();
			int lastIdx = mappedStatementId.lastIndexOf(".");
			Class<?> realClass = Class.forName(mappedStatementId.substring(0, lastIdx));
			ClassRepository classRepository = (ClassRepository) ReflectionUtils.invokeMethod(GET_GENERIC_INFO,
					realClass);
			assert classRepository != null;
			String baseMapperWithGeneric = Arrays.stream(classRepository.getSuperInterfaces())
				.map(Type::getTypeName)
				.filter(x -> x.startsWith(BaseMapper.class.getName()))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Cannot find superInterface '" + BaseMapper.class.getName()
						+ "' subclass is " + "'" + realClass + "'"));

			int startIdx = baseMapperWithGeneric.indexOf("<");
			int endIdx = baseMapperWithGeneric.lastIndexOf(">");
			return Class.forName(baseMapperWithGeneric.substring(startIdx + 1, endIdx));
		}
		catch (Exception e) {
			throw new IllegalStateException(String.format(
					"Parse mybatis-plus exception. Determine db-entity class fail. curr mappedStatement is '%s'.",
					mappedStatement.getId()), e);
		}
	}

	/**
	 * wrapper转换为AbstractWrapper
	 */
	@SuppressWarnings("rawtypes")
	private static AbstractWrapper assertAbstractMapper(Wrapper wrapper) {
		AbstractWrapper abstractWrapper;
		if (wrapper instanceof AbstractWrapper) {
			abstractWrapper = (AbstractWrapper) wrapper;
		}
		else if (wrapper instanceof AbstractChainWrapper) {
			abstractWrapper = ((AbstractChainWrapper) wrapper).getWrapper();
		}
		else {
			throw new IllegalStateException("un-support wrapper type for " + wrapper.getClass().getCanonicalName());
		}

		return abstractWrapper;
	}

}
