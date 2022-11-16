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

package com.art.common.sequence.segment.redis;

import com.art.common.sequence.config.SequenceProperties;
import com.art.common.sequence.exception.SeqException;
import com.art.common.sequence.segment.SeqSegment;
import com.art.common.sequence.segment.SeqSegmentConfig;
import com.art.common.sequence.segment.SeqSegmentManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis号段管理器
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/23 09:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "fxz.common.sequence", value = "type", havingValue = "redis")
public class RedisSeqSegmentManager implements SeqSegmentManager {

	private final StringRedisTemplate redisTemplate;

	private final SequenceProperties sequenceProperties;

	private static Map<String, SeqSegment> seqRangeMap = new ConcurrentHashMap<>(8);

	/**
	 * 获取指定号段名的下一个号段
	 * @param name 号段名
	 * @param seqSegmentConfig 序列号号段配置
	 * @return 返回号段
	 * @throws SeqException 异常
	 */
	@Override
	public SeqSegment nextSegment(String name, SeqSegmentConfig seqSegmentConfig) throws SeqException {
		String redisKey = sequenceProperties.getKeyPrefix() + name;

		SeqSegment seqSegment = seqRangeMap.get(redisKey);

		if (Objects.isNull(seqSegment)) {
			// 如果缓存中不存在此业务key 进行缓存
			redisTemplate.opsForValue().setIfAbsent(redisKey, String.valueOf(seqSegmentConfig.getRangeStart()));
		}

		// 号段步长
		Integer rangeStep = seqSegmentConfig.getRangeStep();

		// 号段的序列号开始值
		Long max = redisTemplate.opsForValue().increment(redisKey, rangeStep);
		// 号段的序列号结束值
		Long min = max - rangeStep;

		seqSegment = new SeqSegment(min, max, seqSegmentConfig.getStep());

		seqRangeMap.put(redisKey, seqSegment);

		return seqSegment;
	}

}
