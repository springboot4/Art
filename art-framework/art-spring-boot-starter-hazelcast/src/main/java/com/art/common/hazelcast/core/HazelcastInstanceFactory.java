package com.art.common.hazelcast.core;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * HazelcastInstanceFactory 用于创建 HazelcastInstance
 *
 * @author fxz
 */
@RequiredArgsConstructor
public class HazelcastInstanceFactory implements FactoryBean<HazelcastInstance> {

	private final HazelcastProperties properties;

	public HazelcastInstance getInstance() {
		// 配置实例名称
		Config config = new Config(properties.getInstanceName());
		// 配置集群名称
		config.setClusterName(properties.getClusterName());
		// 配置网络
		configNetwork(config.getNetworkConfig());
		// 创建实例
		return Hazelcast.newHazelcastInstance(config);
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

}
