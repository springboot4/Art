package com.fxz.common.lock.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

import static com.fxz.common.lock.constant.RedisConstant.REDIS_PREFIX;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 16:26
 * @see <a href=
 * "https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95"/>
 */
@RequiredArgsConstructor
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonClientAutoConfiguration {

	private final RedisProperties redisProperties;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();

		// 哨兵模式
		if (Objects.nonNull(redisProperties.getSentinel())) {
			// 哨兵配置
			initSentinelConfig(config);
		}
		else if (Objects.nonNull(redisProperties.getCluster())) {
			// 集群配置
			initClusterConfig(config);
		}
		else {
			// 单节点配置
			initSingleConfig(config);

		}

		return Redisson.create(config);
	}

	/**
	 * redis单节点配置
	 *
	 * @see <a href=
	 * "https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95#26-%E5%8D%95redis%E8%8A%82%E7%82%B9%E6%A8%A1%E5%BC%8F"/>
	 */
	private void initSingleConfig(Config config) {
		SingleServerConfig singleServerConfig = config.useSingleServer();

		// 节点地址
		singleServerConfig.setAddress(REDIS_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort())
				.setDatabase(redisProperties.getDatabase());

		// 密码
		if (redisProperties.getPassword() != null) {
			singleServerConfig.setPassword(redisProperties.getPassword());
		}
	}

	/**
	 * redis集群配置
	 *
	 * @see <a href=
	 * "https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95#24-%E9%9B%86%E7%BE%A4%E6%A8%A1%E5%BC%8F"/>
	 */
	private void initClusterConfig(Config config) {
		ClusterServersConfig clusterServersConfig = config.useClusterServers();

		// 集群节点地址
		clusterServersConfig.addNodeAddress(redisProperties.getCluster().getNodes().stream()
				.map(node -> REDIS_PREFIX + node).toArray(String[]::new));

		// 密码
		if (redisProperties.getPassword() != null) {
			clusterServersConfig.setPassword(redisProperties.getPassword());
		}
	}

	/**
	 * redis哨兵配置
	 *
	 * @see <a href=
	 * "https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95#27-%E5%93%A8%E5%85%B5%E6%A8%A1%E5%BC%8F"/>
	 */
	private void initSentinelConfig(Config config) {
		SentinelServersConfig sentinelServersConfig = config.useSentinelServers();

		// 节点信息
		sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster())
				.addSentinelAddress(redisProperties.getSentinel().getNodes().toArray(new String[0]));

		// 密码
		if (redisProperties.getPassword() != null) {
			sentinelServersConfig.setPassword(redisProperties.getPassword());
		}
	}

}
