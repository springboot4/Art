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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * HazelcastProperties
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/3/23 16:16
 */
@Data
@ConfigurationProperties(prefix = HazelcastProperties.PREFIX)
public class HazelcastProperties {

	/**
	 * 配置文件前缀
	 */
	public static final String PREFIX = "art.hazelcast";

	public static final String SERVER = "art.hazelcast.server.enable";

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
