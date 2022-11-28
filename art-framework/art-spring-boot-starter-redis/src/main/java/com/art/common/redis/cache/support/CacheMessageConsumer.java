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

package com.art.common.redis.cache.support;

import com.art.common.mq.redis.pubsub.AbstractPubSubMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fxz
 */
@Slf4j
@RequiredArgsConstructor
public class CacheMessageConsumer extends AbstractPubSubMessageListener<CacheMessage> {

	private final RedisCaffeineCacheManager redisCaffeineCacheManager;

	@Override
	public void onMessage(CacheMessage cacheMessage) {
		log.info("接收到redis topic消息，清空本地缓存，缓存名称:{}, key:{}", cacheMessage.getCacheName(), cacheMessage.getKey());
		redisCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());
	}

}