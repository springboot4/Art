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

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.ToString;
import org.springframework.beans.factory.DisposableBean;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * HazelcastClientFactory 用于创建 Hazelcast节点实例
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/3/23 16:16
 */
@ToString
public class HazelcastServerInstance implements DisposableBean {

	private final HazelcastProperties properties;

	private HazelcastInstance instance;

	public HazelcastServerInstance(HazelcastProperties properties) {
		this.properties = properties;

		// 配置实例名称
		Config config = new Config(properties.getInstanceName());
		// 配置集群名称
		config.setClusterName(properties.getClusterName());
		// 配置网络
		configNetwork(config.getNetworkConfig());
		// 创建实例
		instance = Hazelcast.newHazelcastInstance(config);
	}

	private void configNetwork(NetworkConfig networkConfig) {
		// 实例端口配置，端口自增
		networkConfig.setPort(properties.getPort().get(0)).setPortAutoIncrement(true);
		// 禁用AWS云，使用TCP/IP协议进行成员发现和通信
		networkConfig.getJoin().getAwsConfig().setEnabled(false);
		// 禁止多播，Hazelcast集群节点将无法自动加入到集群中
		networkConfig.getJoin().getMulticastConfig().setEnabled(false);
		// 启用TCP/IP协议，通过指定节点地址信息，Hazelcast节点将通过TCP/IP协议加入到集群中
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
	public void destroy() {
		instance.shutdown();
	}

}
