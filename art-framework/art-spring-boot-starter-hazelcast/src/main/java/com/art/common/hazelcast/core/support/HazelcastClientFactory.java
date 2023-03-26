/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.hazelcast.core.support;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * HazelcastClientFactory 用于创建 Hazelcast节点实例
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/3/23 16:16
 */
@RequiredArgsConstructor
public class HazelcastClientFactory implements FactoryBean<HazelcastInstance>, DisposableBean {

	private final HazelcastProperties properties;

	private HazelcastInstance client;

	public HazelcastInstance getInstance() {
		if (Objects.nonNull(client)) {
			return client;
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

		return client;
	}

	private void configConnectionStrategy(ClientConnectionStrategyConfig connectionStrategyConfig) {
		connectionStrategyConfig.setAsyncStart(false)
			.setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);
	}

	private void configNetwork(ClientNetworkConfig networkConfig) {
		networkConfig.addAddress(address()).setSmartRouting(true).setConnectionTimeout(5000);
	}

	private String[] address() {
		StringJoiner sj = new StringJoiner(",");
		properties.getMembers()
			.stream()
			.flatMap(m -> properties.getPort().stream().map(p -> m + ":" + p))
			.forEach(sj::add);
		return sj.toString().split(",");
	}

	@Override
	public HazelcastInstance getObject() throws Exception {
		return getInstance();
	}

	@Override
	public Class<?> getObjectType() {
		return HazelcastInstance.class;
	}

	@Override
	public void destroy() {
		client.shutdown();
	}

}
