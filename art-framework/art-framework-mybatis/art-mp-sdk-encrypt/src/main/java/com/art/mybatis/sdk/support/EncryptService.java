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

import com.art.mybatis.sdk.base.EncryptExecutor;
import com.art.mybatis.sdk.func.DecryptFunc;
import com.art.mybatis.sdk.func.EncryptFunc;
import com.art.mybatis.sdk.util.MybatisPlusSupportUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.lang.Nullable;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 加解密服务
 *
 * @author fxz
 */
@Slf4j
public class EncryptService implements SmartInitializingSingleton {

	/**
	 * 临时缓存
	 */
	@SuppressWarnings("rawtypes")
	private static final Map<Class, Method[]> TMP_CLAZZ_METHOD_CACHE = new ConcurrentHashMap<>(64);

	/**
	 * MappedStatement与加解密上下文缓存
	 */
	private final Map<MappedStatement, EncryptInfoContext> statementAndEncryptInfoCache = new ConcurrentHashMap<>(128);

	/**
	 * MappedStatementId与加解密上下文缓存
	 */
	private final Map<String, EncryptInfoContext> statementIdAndEncryptInfoCache = new ConcurrentHashMap<>(128);

	@Getter
	private final List<SqlSessionFactory> sqlSessionFactoryList;

	@Getter
	private final EncryptFunc encryptFunc;

	@Getter
	private final DecryptFunc decryptFunc;

	public EncryptService(List<SqlSessionFactory> sqlSessionFactoryList, EncryptExecutor encryptExecutor) {
		this.encryptFunc = new EncryptFunc(encryptExecutor);
		this.decryptFunc = new DecryptFunc(encryptExecutor);
		this.sqlSessionFactoryList = sqlSessionFactoryList;
	}

	/**
	 * 根据MappedStatement获取加解密上下文
	 * @param mappedStatement mappedStatement
	 * @return 对应的加解密上下文
	 */
	@Nullable
	public EncryptInfoContext getEncryptInfoHolderByMappedStatement(MappedStatement mappedStatement) {
		return statementAndEncryptInfoCache.get(mappedStatement);
	}

	/**
	 * 根据MappedStatementId获取加解密上下文
	 * @param mappedStatementId mappedStatementId
	 * @return 对应的加解密上下文
	 */
	@Nullable
	public EncryptInfoContext getEncryptInfoHolderByMappedStatementId(String mappedStatementId) {
		return statementIdAndEncryptInfoCache.get(mappedStatementId);
	}

	/**
	 * 加密
	 */
	public Object doEncrypt(MappedStatement mappedStatement, Object parameter, EncryptInfoContext encryptInfoContext) {
		return encryptFunc.doEncrypt(mappedStatement, parameter, encryptInfoContext);
	}

	/**
	 * 解密
	 */
	public Object doDecrypt(MappedStatement mappedStatement, Object rowResult, EncryptInfoContext encryptInfoContext) {
		return decryptFunc.doDecrypt(mappedStatement, rowResult, encryptInfoContext);
	}

	/**
	 * MappedStatement与对应的加解密信息构建并缓存
	 */
	@Override
	public void afterSingletonsInstantiated() {
		StopWatch stopWatch = new StopWatch("encrypt parser");

		// 获取mybatis配置
		stopWatch.start("get configuration");
		List<Configuration> configurationList = sqlSessionFactoryList.stream()
			.map(SqlSessionFactory::getConfiguration)
			.collect(Collectors.toList());
		log.info("EncryptService 获取configurationList: {}.", configurationList.size());
		stopWatch.stop();

		// 获取sql对应的MappedStatement
		stopWatch.start("get mappedStatementIdSet");
		Set<MappedStatement> mappedStatementSet = configurationList.stream().flatMap(x -> {
			Collection<MappedStatement> mappedStatements = x.getMappedStatements();
			Object[] objects = mappedStatements.toArray();
			List<MappedStatement> list = new ArrayList<>(16);
			for (Object object : objects) {
				if (object instanceof MappedStatement) {
					list.add((MappedStatement) object);
				}
			}
			return list.stream();
		}).filter(x -> !x.getId().contains("!")).collect(Collectors.toSet());
		log.info("EncryptService 获取mappedStatementSet: {}.",
				mappedStatementSet.stream().map(MappedStatement::getId).collect(Collectors.toList()));
		stopWatch.stop();

		// 获取sql与其对应的Method
		stopWatch.start("get statementMethodMap");
		Map<MappedStatement, Method> statementMethodMap = mappedStatementSet.stream()
			.collect(Collectors.toMap(Function.identity(), this::getMethodByMappedStatement));
		stopWatch.stop();

		// 构建所有MappedStatement的加解密上下文
		stopWatch.start("get encryptInfoContextList");
		List<EncryptInfoContext> encryptInfoContextList = mappedStatementSet.stream()
			.map(mappedStatement -> EncryptInfoContext.Builder.build(mappedStatement,
					statementMethodMap.get(mappedStatement)))
			.collect(Collectors.toList());
		stopWatch.stop();

		// MappedStatement和加密上下文的映射
		stopWatch.start("do completely");
		Map<MappedStatement, EncryptInfoContext> allEncryptDecryptInfoMap = encryptInfoContextList.stream()
			.collect(Collectors.toMap(EncryptInfoContext::getMappedStatement, Function.identity()));
		statementAndEncryptInfoCache.putAll(allEncryptDecryptInfoMap);
		Map<String, EncryptInfoContext> encryptDecryptInfoMap = encryptInfoContextList.stream()
			.filter(x -> x.getMappedStatement() != null)
			.collect(Collectors.toMap(x -> x.getMappedStatement().getId(), Function.identity()));
		statementIdAndEncryptInfoCache.putAll(encryptDecryptInfoMap);
		log.info("[EncryptService] parse end. Obtain allEncryptDecryptInfoMap {}, allEncryptDecryptInfoMap {}.",
				allEncryptDecryptInfoMap, encryptDecryptInfoMap);
		stopWatch.stop();

		// 清除临时缓存
		TMP_CLAZZ_METHOD_CACHE.clear();
		log.info("[EncryptService] time-consuming statistics {}.", stopWatch);
	}

	/**
	 * 根据mappedStatement获取对应的方法（主要解决重载方法的识别问题）
	 * @param mappedStatement mappedStatement
	 * @return mappedStatement对应的方法
	 */
	private Method getMethodByMappedStatement(MappedStatement mappedStatement) {
		try {
			// mappedStatementId
			String mappedStatementId = mappedStatement.getId();
			int lastDotIndex = mappedStatementId.lastIndexOf(".");

			// 当前类
			Class<?> targetClass = Class.forName(mappedStatementId.substring(0, lastDotIndex));

			// 当前类以及所有拥有的方法
			Method[] allPublicMethods = TMP_CLAZZ_METHOD_CACHE.computeIfAbsent(targetClass, Class::getMethods);

			// 对应的方法名称
			String targetMethodName = mappedStatementId.substring(lastDotIndex + 1);

			// 同名方法
			List<Method> targetMethodList = Arrays.stream(allPublicMethods)
				.filter(x -> targetMethodName.equals(x.getName()))
				.collect(Collectors.toList());
			if (targetMethodList.size() > 1) {
				// 处理BaseMapper中的重载方法
				targetMethodList = MybatisPlusSupportUtil.filterMethod(mappedStatement, targetMethodList);
			}
			if (targetMethodList.size() != 1) {
				throw new IllegalStateException(
						String.format("Base mappedStatement, Except find method [%s] 1, but actual find %s",
								mappedStatementId, targetMethodList.size()));
			}

			return targetMethodList.get(0);
		}
		catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

}
