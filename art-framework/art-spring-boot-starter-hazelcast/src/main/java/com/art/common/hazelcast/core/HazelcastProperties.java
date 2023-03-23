package com.art.common.hazelcast.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * HazelcastProperties
 * @author fxz
 */
@Data
@ConfigurationProperties(prefix = HazelcastProperties.PREFIX)
public class HazelcastProperties {

	/**
	 * 配置文件前缀
	 */
	public static final String PREFIX = "art.hazelcast";

	/**
	 * Hazelcast实例名称
	 */
	private String instanceName = "fxz-instance";

	/**
	 * Hazelcast集群名称
	 */
	private String clusterName = "fxz-cluster";

	/**
	 * Hazelcast实例端口
	 */
	private List<Integer> port = Collections.singletonList(5701);

	/**
	 * Hazelcast实例ip
	 */
	private List<String> members = Collections.singletonList("127.0.0.1");

}
