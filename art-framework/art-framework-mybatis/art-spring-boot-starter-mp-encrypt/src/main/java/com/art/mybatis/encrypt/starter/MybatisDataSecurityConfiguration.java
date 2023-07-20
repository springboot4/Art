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

package com.art.mybatis.encrypt.starter;

import com.art.mybatis.sdk.base.AesEncryptExecutor;
import com.art.mybatis.sdk.base.EncryptExecutor;
import com.art.mybatis.sdk.interceptor.MybatisEncryptPlugin;
import com.art.mybatis.sdk.propertie.EncryptProperties;
import com.art.mybatis.sdk.support.EncryptService;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fxz
 */
@EnableConfigurationProperties(EncryptProperties.class)
@AutoConfiguration
@AutoConfigureAfter(value = { Configuration.class, SqlSessionFactory.class })
public class MybatisDataSecurityConfiguration {

	/**
	 * aes加密
	 */
	@Bean
	public AesEncryptExecutor aesEncryptExecutor(EncryptProperties encryptProperties) {
		return new AesEncryptExecutor(encryptProperties);
	}

	/**
	 * 加密服务
	 */
	@Bean
	public EncryptService encryptService(List<SqlSessionFactory> sqlSessionFactoryList,
			EncryptExecutor encryptExecutor) {
		EncryptService encryptService = new EncryptService(sqlSessionFactoryList, encryptExecutor);
		// 添加mybatis拦截器
		addInterceptor(encryptService);
		return encryptService;
	}

	/**
	 * 添加mybatis拦截器
	 */
	@SuppressWarnings("all")
	private void addInterceptor(EncryptService encryptService) {
		MybatisEncryptPlugin mybatisEncryptPlugin = new MybatisEncryptPlugin(encryptService);

		encryptService.getSqlSessionFactoryList().forEach(sqlSessionFactory -> {
			Configuration configuration = sqlSessionFactory.getConfiguration();
			if (configuration == null) {
				return;
			}

			List<Interceptor> interceptors = new ArrayList<>(configuration.getInterceptors());
			int mybatisPlusInterceptorIdx = -1;
			for (int i = 0; i < interceptors.size(); i++) {
				if (interceptors.get(i) instanceof MybatisPlusInterceptor) {
					mybatisPlusInterceptorIdx = i;
					break;
				}
			}
			if (mybatisPlusInterceptorIdx >= 0) {
				interceptors.add(mybatisPlusInterceptorIdx, mybatisEncryptPlugin);
			}
			else {
				interceptors.add(mybatisEncryptPlugin);
			}

			Field interceptorChainField = ReflectionUtils.findField(Configuration.class, "interceptorChain");
			boolean icfAccessible = interceptorChainField.isAccessible();
			interceptorChainField.setAccessible(true);
			InterceptorChain interceptorChain = (InterceptorChain) ReflectionUtils.getField(interceptorChainField,
					configuration);

			Field interceptorsField = ReflectionUtils.findField(InterceptorChain.class, "interceptors");
			boolean ifAccessible = interceptorsField.isAccessible();
			interceptorsField.setAccessible(true);
			List<Interceptor> finalInterceptors = (List<Interceptor>) ReflectionUtils.getField(interceptorsField,
					interceptorChain);

			interceptorChainField.setAccessible(icfAccessible);
			interceptorsField.setAccessible(ifAccessible);

			assert finalInterceptors != null;

			finalInterceptors.clear();
			finalInterceptors.addAll(interceptors);
		});
	}

}
