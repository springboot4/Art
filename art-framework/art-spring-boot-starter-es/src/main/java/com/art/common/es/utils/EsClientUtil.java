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

package com.art.common.es.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/8/28 12:28
 */
@Component
public class EsClientUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	/**
	 * 根据id获取索引下的单条文档
	 * @param index 索引
	 * @param id 索引id
	 * @param tClass 文档类型
	 */
	@SneakyThrows
	public static <T> T getById(String index, String id, Class<T> tClass) {
		Assert.isTrue(StrUtil.isNotBlank(index) && StrUtil.isNotBlank(id));

		return getClient().get(g -> g.index(index).id(id), tClass).source();
	}

	/**
	 * 根据id更新索引下的单条文档
	 * @param index 索引
	 * @param id 索引id
	 * @param doc 文档内容
	 * @param tClass 文档类型
	 */
	@SneakyThrows
	public static <T> void updateById(String index, String id, T doc, Class<T> tClass) {
		Assert.isTrue(StrUtil.isNotBlank(index) && StrUtil.isNotBlank(id));

		getClient().update(u -> u.index(index).id(id).doc(doc), tClass);
	}

	public static ElasticsearchClient getClient() {
		return applicationContext.getBean(ElasticsearchClient.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		EsClientUtil.applicationContext = applicationContext;
	}

}
