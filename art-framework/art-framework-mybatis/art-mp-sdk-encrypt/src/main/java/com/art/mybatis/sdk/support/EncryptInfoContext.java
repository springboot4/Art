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

package com.art.mybatis.sdk.support;

import com.art.mybatis.sdk.annotation.EncryptionData;
import com.art.mybatis.sdk.enums.TypeEnum;
import com.art.mybatis.sdk.info.BeanEncryptDetailInfo;
import com.art.mybatis.sdk.info.ParamEncryptDetailInfo;
import com.art.mybatis.sdk.util.MybatisPlusSupportUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

import static com.art.mybatis.sdk.enums.TypeEnum.*;

/**
 * 加解密信息上下文
 *
 * @author fxz
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptInfoContext {

	/**
	 * Sql对应的MappedStatement
	 */
	private MappedStatement mappedStatement;

	/**
	 * 是否需要加密
	 */
	private boolean needEncrypt;

	/**
	 * 要加密的bean信息
	 */
	private List<BeanEncryptDetailInfo> encryptBeanInfoList;

	/**
	 * 要加密的参数信息
	 */
	private ParamEncryptDetailInfo encryptParamInfo;

	/**
	 * 是否需要解密
	 */
	private boolean needDecrypt;

	/**
	 * 要解密的bean信息
	 */
	private BeanEncryptDetailInfo decryptBeanInfo;

	/**
	 * 加密信息上下文构造器
	 */
	@Slf4j
	public static class Builder {

		/**
		 * 泛型左符号
		 */
		private static final String GENERIC_LEFT_SIGN = StringPool.LEFT_CHEV;

		/**
		 * 泛型右符号
		 */
		private static final String GENERIC_RIGHT_SIGN = StringPool.RIGHT_CHEV;

		/**
		 * 获取方法签名的Method
		 */
		private static final Method GET_GENERIC_SIGNATURE;

		static {
			// 通过反射获取方法的签名信息
			GET_GENERIC_SIGNATURE = ReflectionUtils.findMethod(Method.class, "getGenericSignature");
			// noinspection ConstantConditions
			GET_GENERIC_SIGNATURE.setAccessible(true);
		}

		/**
		 * 构建MappedStatement对应的加解密上下文
		 * @param mappedStatement MappedStatement
		 * @param targetMethod MappedStatement对应的Method
		 * @return MappedStatement对应的加解密上下文
		 */
		public static EncryptInfoContext build(MappedStatement mappedStatement, Method targetMethod) {
			// 方法参数
			Parameter[] parameters = targetMethod.getParameters();

			// 方法参数上的注解
			Annotation[][] paramAnnotations = targetMethod.getParameterAnnotations();

			// 方法的返回值类型
			Class<?> returnClass = targetMethod.getReturnType();

			// 参数与其对应的注解
			Map<Parameter, Annotation[]> parameterAnnotationsMap = new HashMap<>(8);
			for (int i = 0; i < parameters.length; i++) {
				parameterAnnotationsMap.put(parameters[i], paramAnnotations[i]);
			}

			// 获取方法的加密信息
			Pair<List<BeanEncryptDetailInfo>, ParamEncryptDetailInfo> encryptPair = getEncryptDetailInfo(
					mappedStatement, targetMethod, parameterAnnotationsMap);
			List<BeanEncryptDetailInfo> encryptBeanInfoList = encryptPair.getLeft();
			ParamEncryptDetailInfo encryptParamInfo = encryptPair.getRight();
			// 是否需要加密
			boolean needEncrypt = CollectionUtils.isNotEmpty(encryptBeanInfoList)
					|| CollectionUtils.isNotEmpty(encryptParamInfo.getParamEncryptMap());

			// 获取方法的解密信息
			BeanEncryptDetailInfo decryptBeanInfo = getDecryptDetailInfo(mappedStatement, targetMethod, returnClass);
			// 是否需要解密
			boolean needDecrypt = Objects.nonNull(decryptBeanInfo)
					&& CollectionUtils.isNotEmpty(decryptBeanInfo.getFieldEncryptMap());

			// 组装MappedStatement对应的加解密上下文
			EncryptInfoContext instance = new EncryptInfoContext();
			instance.setMappedStatement(mappedStatement);
			instance.setEncryptBeanInfoList(encryptBeanInfoList);
			instance.setEncryptParamInfo(encryptParamInfo);
			instance.setNeedEncrypt(needEncrypt);
			instance.setDecryptBeanInfo(decryptBeanInfo);
			instance.setNeedDecrypt(needDecrypt);
			log.debug("EncryptInfoContext$Factory 解析 mappedStatementId [{}], 创建 instance -> {}", mappedStatement,
					instance);

			return instance;
		}

		/**
		 * 获取方法对应的加密信息
		 * @param mappedStatement mappedStatement
		 * @param targetMethod mappedStatement对应的方法
		 * @param paramAnnotationMap 参数和参数对应的注解
		 * @return 加密信息详情 包含bean的加密信息以及参数的加密信息
		 */
		private static Pair<List<BeanEncryptDetailInfo>, ParamEncryptDetailInfo> getEncryptDetailInfo(
				MappedStatement mappedStatement, Method targetMethod, Map<Parameter, Annotation[]> paramAnnotationMap) {
			// MappedStatement中对应的bean需要加密的信息
			List<BeanEncryptDetailInfo> encryptBeanInfoList = new ArrayList<>(8);
			// MappedStatement中对应的参数需要加密的信息
			ParamEncryptDetailInfo encryptParamInfo = new ParamEncryptDetailInfo();

			// 处理BaseMapper中内置的方法
			if (MybatisPlusSupportUtil.isMybatisPlusMethod(targetMethod)) {
				// Mp内置的方法参数上不可能含有我们自定义的加密注解 所以只判断参数是否是包含T类型
				boolean needParseDbEntity = MybatisPlusSupportUtil.parameterContainDbEntity(targetMethod);
				// 参数包含T类型 说明我们需要判断bean是否需要加密
				if (needParseDbEntity) {
					// 解析MappedStatement对应的class类型
					Class<?> type = MybatisPlusSupportUtil.parseMybatisPlusDbEntityClass(mappedStatement);
					// 构建bean的加密信息
					BeanEncryptDetailInfo info = BeanEncryptDetailInfo.build(type);
					// 如果有添加加密注解的字段 保存到集合中
					if (CollectionUtils.isNotEmpty(info.getFieldEncryptMap())) {
						encryptBeanInfoList.add(info);
					}
				}

				// 直接返回即可
				return Pair.of(encryptBeanInfoList, encryptParamInfo);
			}

			// 保存参数名称与注解信息
			Map<String, EncryptionData> paramEncryptMap = new HashMap<>(8);
			// 参数加密信息
			encryptParamInfo.setParamEncryptMap(paramEncryptMap);

			// 对于不是MP内置的方法 我们从参数入手 看一下是否有标识了加密注解的实体类以及标识了加密注解的方法参数
			paramAnnotationMap.forEach((param, annotations) -> {
				// 参数类型
				Class<?> type = param.getType();
				// 参数类型
				TypeEnum typeEnum = TypeEnum.parseType(type);

				// 加密注解用于方法参数时 只能用于String类型
				if (typeEnum != STRING) {
					boolean hasEncryptionData = Arrays.stream(annotations)
						.anyMatch(annotation -> annotation instanceof EncryptionData);
					if (hasEncryptionData) {
						throw new IllegalArgumentException(String
							.format("@EncryptionData 只能用于String类型 不能用于: [%s]. @see %s", type, mappedStatement.getId()));
					}
				}

				// 如果是基础类型或者是其包装类型就跳过 因为我们只处理两类，即：标识了加密注解的方法参数以及标识了加密注解的实体类参数
				if (typeEnum == PRIMITIVE_OR_WRAPPER) {
					return;
				}

				// 如果参数是String类型 我们看一下是个标识了加密注解以及@Param注解
				if (typeEnum == STRING) {
					// 查找@EncryptionData注解
					EncryptionData encryptionDataAnnotation = (EncryptionData) Arrays.stream(annotations)
						.filter(x -> x instanceof EncryptionData)
						.findFirst()
						.orElse(null);
					// 查找@Param注解
					Param paramAnnotation = (Param) Arrays.stream(annotations)
						.filter(x -> x instanceof Param)
						.findFirst()
						.orElse(null);

					// 当@EncryptionData应用于String类型的参数前 需同时使用@Param指定名称
					if (Objects.nonNull(encryptionDataAnnotation)) {
						if (Objects.isNull(paramAnnotation)) {
							throw new IllegalArgumentException(String.format(
									" 当@EncryptionData应用于ElementType.PARAMETER前时 还需同时使用@Param指定名称。 @see %s",
									mappedStatement.getId()));
						}

						// 保存参数名称与注解信息
						paramEncryptMap.put(paramAnnotation.value(), encryptionDataAnnotation);
					}
					return;
				}

				// 如果参数是Map、Collection、数组 我们看一下他的泛型 即元素对应的实体类的加密信息
				if (typeEnum == MAP || typeEnum == COLLECTION || typeEnum == ARRAY) {
					// 解析出集合或者数组实际元素的类型
					Pair<TypeEnum, Class<?>> typeEnumClassPair = parseInnermostType(param, mappedStatement);
					// 不是自定义的java bean 可以直接返回了 因为存在我们要处理的加注解的bean信息
					if (typeEnumClassPair.getLeft() != CUSTOM_BEAN) {
						return;
					}

					// 构建bean的加密信息
					BeanEncryptDetailInfo info = BeanEncryptDetailInfo.build(typeEnumClassPair.getRight());
					// 如果有添加加密注解的字段 保存到集合中
					if (!CollectionUtils.isEmpty(info.getFieldEncryptMap())) {
						encryptBeanInfoList.add(info);
					}
				}

				// 如果方法参数是自定义bean 那么我们处理他的加密信息
				if (typeEnum == CUSTOM_BEAN) {
					// 构建bean的加密信息
					BeanEncryptDetailInfo info = BeanEncryptDetailInfo.build(type);
					// 如果有添加加密注解的字段 保存到集合中
					if (!CollectionUtils.isEmpty(info.getFieldEncryptMap())) {
						encryptBeanInfoList.add(info);
					}
				}
				else {
					log.debug("忽略分析的类型: [{}]. @see {}", type.getName(), mappedStatement.getId());
				}
			});

			return Pair.of(encryptBeanInfoList, encryptParamInfo);
		}

		/**
		 * 获取方法对应的解密信息
		 * @param mappedStatement mappedStatement
		 * @param targetMethod mappedStatement对应的方法
		 * @param returnClass 方法返回值类型
		 * @return 解密信息详情 方法返回值对应的解密信息
		 */
		@Nullable
		private static BeanEncryptDetailInfo getDecryptDetailInfo(MappedStatement mappedStatement, Method targetMethod,
				Class<?> returnClass) {
			// 处理Mp内置的方法 分析返回值是否包含T即可
			if (MybatisPlusSupportUtil.isMybatisPlusMethod(targetMethod)) {
				// 方法的返回值包含T 则说明可能需要解密
				boolean needParseDbEntity = MybatisPlusSupportUtil.returnClassContainDbEntity(targetMethod);
				if (!needParseDbEntity) {
					return null;
				}

				// 解析出MappedStatement对应的实体类类型
				Class<?> type = MybatisPlusSupportUtil.parseMybatisPlusDbEntityClass(mappedStatement);

				// 构建bean的解密信息 直接返回
				return BeanEncryptDetailInfo.build(type);
			}

			// 返回值类型
			TypeEnum typeEnum = TypeEnum.parseType(returnClass);

			// 返回值类型是自定义的java bean 直接处理bean的解密信息
			if (typeEnum == CUSTOM_BEAN) {
				return BeanEncryptDetailInfo.build(returnClass);
			}

			// 如果返回值类型是Map或者Collection 那么我们分析他的泛型信息中是否包含需要解密的实体类
			if (typeEnum == MAP || typeEnum == COLLECTION) {
				// 方法的签名信息
				Object getGenericSignature = ReflectionUtils.invokeMethod(GET_GENERIC_SIGNATURE, targetMethod);
				// 返回值无指定泛型 则无需解密
				if (Objects.isNull(getGenericSignature)) {
					return null;
				}

				// 方法签名字符串
				String signature = getGenericSignature.toString();

				// 处理返回值没有泛型信息 但是参数有泛型信息
				String returnObjSignature = signature.substring(signature.lastIndexOf(")") + 1);
				int startIndex = returnObjSignature.indexOf(GENERIC_LEFT_SIGN);
				int endIndex = returnObjSignature.lastIndexOf(GENERIC_RIGHT_SIGN);
				if (startIndex == -1 || endIndex == -1) {
					// 走到这里说明只有入参存在泛型 返回值没有泛型信息 不需要解密 直接返回
					return null;
				}

				// 取到方法返回值的泛型信息
				String genericInfo = returnObjSignature.substring(startIndex + 1, endIndex);
				// 不支持泛型嵌套泛型这样的复杂写法 快速失败 使项目启动不起来
				if (genericInfo.contains(GENERIC_LEFT_SIGN) || genericInfo.contains(GENERIC_RIGHT_SIGN)) {
					throw new UnsupportedOperationException(String.format("不支持的嵌套泛型写法。 @see %s", targetMethod));
				}

				// 逻辑到这里 已经保证了返回值的有泛型 Map获取第二个 Collection直接获取第一个
				int genericInfoIndex = typeEnum == MAP ? 1 : 0;
				String onlyGenericInfo = genericInfo.split(";")[genericInfoIndex];
				// 不允许泛型是数组 快速失败 使项目启动不起来
				if (onlyGenericInfo.startsWith("[")) {
					throw new UnsupportedOperationException(String.format("不允许泛型是数组. @see %s", targetMethod));
				}

				/*
				 * 获取实际的类型
				 */
				String elementClassName = onlyGenericInfo.substring(1).replace("/", ".");
				Class<?> elementClass;
				try {
					elementClass = Class.forName(elementClassName);
				}
				catch (ClassNotFoundException e) {
					throw new IllegalStateException(
							String.format("Cannot find class [%s], while signature is [%s]. targetMethod is [%s].",
									elementClassName, signature, targetMethod),
							e);
				}

				// 返回值类型
				typeEnum = TypeEnum.parseType(elementClass);

				// 只处理自定义java bean的泛型的加密信息
				return typeEnum == CUSTOM_BEAN ? BeanEncryptDetailInfo.build(elementClass) : null;
			}

			// 如果返回值类型是数组 那么我们分析他的元素类型信息
			if (typeEnum == ARRAY) {
				String clazzName = returnClass.getName();

				// 返回值类型不能是多维数组 快速失败
				if (clazzName.startsWith("[[")) {
					throw new UnsupportedOperationException(String
						.format("Cannot support return-type is multidimensional Array. @see %s", targetMethod));
				}

				if (!clazzName.startsWith("[L")) {
					// 如果不是对象数组 (如:是基本类型数组)的话，直接返回null
					return null;
				}

				// 去掉开头的[L和结尾;号
				clazzName = clazzName.substring(2, clazzName.length() - 1);
				Class<?> beanClass;
				try {
					// 获取实际的类信息
					beanClass = Class.forName(clazzName);
				}
				catch (ClassNotFoundException e) {
					throw new IllegalStateException(
							String.format("Cannot find class [%s], targetMethod is [%s].", clazzName, targetMethod), e);
				}

				// 类型信息
				typeEnum = TypeEnum.parseType(beanClass);

				// 只处理自定义java bean的泛型的加密信息
				return typeEnum == CUSTOM_BEAN ? BeanEncryptDetailInfo.build(beanClass) : null;
			}

			log.info("忽略处理的解密信息:{}", returnClass.getName());
			return null;
		}

		/**
		 * 解析parameter泛型里层数据类型 主要是获取数组、collection、map里的泛型信息
		 * @param parameter 待解析的参数信息
		 * @param mappedStatement mappedStatement
		 * @return parameter泛型里层数据类型
		 */
		private static Pair<TypeEnum, Class<?>> parseInnermostType(Parameter parameter,
				MappedStatement mappedStatement) {
			// 参数类型
			Class<?> clazz = parameter.getType();
			TypeEnum typeEnum = TypeEnum.parseType(clazz);

			switch (typeEnum) {
				case PRIMITIVE_OR_WRAPPER:
				case STRING:
				case CUSTOM_BEAN:
				case SYSTEM_BEAN:
					// 这几种情况下不分析泛型信息
					return Pair.of(typeEnum, clazz);
				case COLLECTION:
				case MAP:
					Type parameterizedType = parameter.getParameterizedType();
					if (parameterizedType instanceof ParameterizedTypeImpl) {
						int targetTypeIndex = typeEnum == COLLECTION ? 0 : 1;
						String genericClassName = ((ParameterizedTypeImpl) parameterizedType)
							.getActualTypeArguments()[targetTypeIndex].getTypeName();
						Class<?> genericClass;
						try {
							// 实际的类型信息
							genericClass = Class.forName(genericClassName);
						}
						catch (ClassNotFoundException e) {
							throw new IllegalArgumentException(
									String.format("Cannot load class [%s]. At param [%s]. @see %s", genericClassName,
											parameter.getName(), mappedStatement.getId()));
						}
						// 类型信息
						TypeEnum genericTypeEnum = TypeEnum.parseType(genericClass);
						// 不支持复杂的泛型(如：泛型嵌套泛型等)
						if (genericTypeEnum == MAP || genericTypeEnum == COLLECTION || genericTypeEnum == ARRAY) {
							throw new UnsupportedOperationException(String.format(
									"Cannot support parse complex Generics nested Generics. At param[%s]. @see %s",
									parameter.getName(), mappedStatement.getId()));
						}

						// 返回类型信息
						return Pair.of(genericTypeEnum, genericClass);
					}
					else {
						// 不支持解析非ParameterizedTypeImpl类型的泛型
						throw new UnsupportedOperationException(String.format(
								"Cannot support parse non-ParameterizedTypeImpl Generics. Maybe you should point specific generics at param[%s]. @see %s",
								parameter.getName(), mappedStatement.getId()));
					}
				case ARRAY:
					// 获取数组元素类型信息
					Type arrayComponentType = TypeUtils.getArrayComponentType(clazz);
					if (arrayComponentType instanceof Class<?>) {
						Class<?> arrayComponentClazz = (Class<?>) arrayComponentType;
						TypeEnum arrayComponentTypeEnum = TypeEnum.parseType(arrayComponentClazz);
						// 不支持复杂的数组嵌套
						if (arrayComponentTypeEnum == MAP || arrayComponentTypeEnum == COLLECTION
								|| arrayComponentTypeEnum == ARRAY) {
							throw new UnsupportedOperationException(
									String.format("Cannot support parse complex nesting Array. At param[%s]. @see %s",
											parameter.getName(), mappedStatement.getId()));
						}

						// 返回类型信息
						return Pair.of(arrayComponentTypeEnum, arrayComponentClazz);
					}
					else {
						// fail fast不支持解析非Class的实现
						throw new UnsupportedOperationException(
								String.format("Cannot support parse non-Class Type. At param[%s]. @see %s",
										parameter.getName(), mappedStatement.getId()));
					}
				default:
					throw new UnsupportedOperationException(
							String.format("Cannot support for typeEnum [%s] At param [%s]. @see %s", typeEnum,
									parameter.getName(), mappedStatement.getId()));
			}

		}

	}

}
