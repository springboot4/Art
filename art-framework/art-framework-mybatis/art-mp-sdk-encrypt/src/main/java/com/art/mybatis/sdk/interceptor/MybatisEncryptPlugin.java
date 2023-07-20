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

package com.art.mybatis.sdk.interceptor;

import com.art.mybatis.sdk.func.EncryptFunc;
import com.art.mybatis.sdk.support.EncryptInfoContext;
import com.art.mybatis.sdk.support.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Objects;

/**
 * 数据库加解密插件
 *
 * @author fxz
 */
@RequiredArgsConstructor
@Slf4j
@Intercepts(
		value = { @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
				@Signature(type = Executor.class, method = "query",
						args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
				@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
						RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }) })
public class MybatisEncryptPlugin implements Interceptor {

	private final EncryptService encryptService;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		if (!(target instanceof Executor)) {
			throw new UnsupportedOperationException("MybatisEncryptPlugin仅支持Executor类型!");
		}

		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String mappedStatementId = mappedStatement.getId();

		// 根据MappedStatement获取相关的加密信息
		EncryptInfoContext encryptInfoContext = encryptService.getEncryptInfoHolderByMappedStatement(mappedStatement);

		// Mybatis Plus的分页count方法
		if (Objects.isNull(encryptInfoContext) && mappedStatementId.endsWith("_mpCount")) {
			String rawMappedStatementId = mappedStatementId.substring(0,
					mappedStatementId.length() - "_mpCount".length());
			encryptInfoContext = encryptService.getEncryptInfoHolderByMappedStatementId(rawMappedStatementId);
			if (encryptInfoContext != null) {
				EncryptFunc encryptFunc = encryptService.getEncryptFunc();
				if (!encryptFunc.existMpCount(rawMappedStatementId)) {
					encryptFunc.putMpCount(rawMappedStatementId, mappedStatementId);
				}
			}
		}

		// encryptInfoHolder为空说明此次不需加解密操作
		if (Objects.isNull(encryptInfoContext)) {
			log.debug("mappedStatementId: [{}] encryptInfoHolder信息为空，不需要加解密。", mappedStatementId);
			// 直接执行数据库操作
			return invocation.proceed();
		}

		// 需要加密
		if (encryptInfoContext.isNeedEncrypt()) {
			log.debug("mappedStatementId [{}] 需要进行加密。", mappedStatementId);
			Object rowParameter = invocation.getArgs()[1];
			invocation.getArgs()[1] = encryptService.doEncrypt(mappedStatement, rowParameter, encryptInfoContext);
		}

		// 执行数据库操作
		Object rowResult = invocation.proceed();

		// 需要解密
		if (encryptInfoContext.isNeedDecrypt()) {
			log.debug("mappedStatementId [{}] 需要进行解密。", mappedStatementId);
			rowResult = encryptService.doDecrypt(mappedStatement, rowResult, encryptInfoContext);
		}

		return rowResult;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}

		return target;
	}

}
