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

package com.art.common.hazelcast.core.support;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * HazelcastInstanceFactory 用于创建 HazelcastInstance
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/3/23 16:16
 */
@RequiredArgsConstructor
public class HazelcastInstanceFactory implements FactoryBean<HazelcastInstance>, DisposableBean {

	private final HazelcastProperties properties;

	private HazelcastInstance instance;

	public HazelcastInstance getInstance() {
		if (Objects.nonNull(instance)) {
			return instance;
		}

		// 配置实例名称
		Config config = new Config(properties.getInstanceName());
		// 配置集群名称
		config.setClusterName(properties.getClusterName());
		// 配置网络
		configNetwork(config.getNetworkConfig());
		// 创建实例
		instance = Hazelcast.newHazelcastInstance(config);

		return instance;
	}

	private void configNetwork(NetworkConfig networkConfig) {
		// 禁止多播，Hazelcast集群节点将无法自动加入到集群中
		networkConfig.getJoin().getMulticastConfig().setEnabled(false);
		// 启用TCP/IP协议，Hazelcast节点将通过TCP/IP协议加入到集群中。
		networkConfig.getJoin().getTcpIpConfig().setEnabled(true).setMembers(members()).setRequiredMember(null);
	}

	private List<String> members() {
		StringJoiner sj = new StringJoiner(",");
		properties.getMembers()
			.stream()
			.flatMap(m -> properties.getPort().stream().map(p -> m + ":" + p))
			.forEach(sj::add);
		return Arrays.asList(sj.toString().split(","));
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
	public void destroy() throws Exception {
		instance.shutdown();
	}

}
