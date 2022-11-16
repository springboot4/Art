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

package com.art.common.sequence.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 发号器相应参数配置
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/23 09:37
 */
@Getter
@Setter
@ConfigurationProperties("fxz.common.sequence")
public class SequenceProperties {

	/**
	 * 存储类型
	 */
	private Type type = Type.MYSQL;

	/**
	 * 序列生成器默认前缀
	 */
	private String keyPrefix = "sequence:";

	/**
	 * 序列生成器默认步长
	 */
	private Integer step = 1;

	/**
	 * 序列生成器默认号段步长
	 */
	private Integer rangeStep = 1000;

	/**
	 * 序列生成器默认号段起始位置
	 */
	private Long rangeStart = 0L;

	/**
	 * 存储类型
	 */
	public enum Type {

		/**
		 * redis
		 */
		REDIS,

		/**
		 * mysql
		 */
		MYSQL;

	}

}
