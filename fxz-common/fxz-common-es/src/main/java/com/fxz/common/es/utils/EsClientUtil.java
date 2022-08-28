package com.fxz.common.es.utils;

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
 * @version 1.0
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
