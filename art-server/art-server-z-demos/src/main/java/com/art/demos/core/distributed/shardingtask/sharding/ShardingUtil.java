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

package com.art.demos.core.distributed.shardingtask.sharding;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.art.demos.core.distributed.shardingtask.core.RedissonDispatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/25 14:46
 */
public class ShardingUtil {

	/**
	 * 获取当前机器所处分片信息
	 * @return RedisSharding
	 */
	public static RedisSharding getSharding() {
		StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

		// 查询出所有机器
		Set<String> keys = redisTemplate.keys("redisson:no:" + "*");
		if (CollUtil.isEmpty(keys)) {
			return null;
		}

		String[] keyArr = keys.toArray(new String[0]);
		Arrays.sort(keyArr);

		// 分片数
		int total = keyArr.length;
		// 机器所处序号
		int index = 0;

		for (int i = 0; i < total; i++) {
			if (keyArr[i].equals("redisson:no:" + RedissonDispatch.NO)) {
				index = i;
				break;
			}
		}

		return new RedisSharding(total, index);
	}

	@AllArgsConstructor
	@Data
	public static class RedisSharding {

		/**
		 * 分片总数
		 */
		private int total;

		/**
		 * 当前机器所处分片
		 */
		private int index;

	}

}
