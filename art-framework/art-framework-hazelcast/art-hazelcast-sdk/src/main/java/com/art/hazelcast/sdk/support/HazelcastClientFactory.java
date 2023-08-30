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

package com.art.hazelcast.sdk.support;

import cn.hutool.core.text.StrPool;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * HazelcastClientFactory 用于创建 Hazelcast连接客户端
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/3/23 16:16
 */
@Slf4j
@RequiredArgsConstructor
public class HazelcastClientFactory implements FactoryBean<HazelcastInstance>, InitializingBean, DisposableBean {

	private final HazelcastProperties properties;

	private final HazelcastServerInstance serverInstance;

	private HazelcastInstance client;

	@Override
	public HazelcastInstance getObject() {
		return this.client;
	}

	@Override
	public Class<?> getObjectType() {
		return HazelcastInstance.class;
	}

	@Override
	public void destroy() {
		client.shutdown();
	}

	@Override
	public void afterPropertiesSet() {
		if (Objects.nonNull(serverInstance)) {
			log.info("本地启动Hazelcast节点:{}", serverInstance);
		}

		ClientConfig clientConfig = new ClientConfig();
		// 配置集群名称
		clientConfig.setClusterName(properties.getClusterName());
		// 配置实例名称
		clientConfig.setInstanceName(properties.getInstanceName());
		// 配置网络
		configNetwork(clientConfig.getNetworkConfig());
		// 配置连接模式
		configConnectionStrategy(clientConfig.getConnectionStrategyConfig());
		// 创建实例
		this.client = HazelcastClient.newHazelcastClient(clientConfig);
	}

	private void configConnectionStrategy(ClientConnectionStrategyConfig connectionStrategyConfig) {
		connectionStrategyConfig.setAsyncStart(false)
			.setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);
	}

	private void configNetwork(ClientNetworkConfig networkConfig) {
		networkConfig.addAddress(address()).setSmartRouting(true).setConnectionTimeout(5000);
	}

	private String[] address() {
		return properties.getMembers()
			.stream()
			.flatMap(m -> properties.getPort().stream().map(p -> m + StrPool.COLON + p))
			.toArray(String[]::new);
	}

}
