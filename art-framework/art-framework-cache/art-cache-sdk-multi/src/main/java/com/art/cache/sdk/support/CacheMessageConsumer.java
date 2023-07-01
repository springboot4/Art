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

package com.art.cache.sdk.support;

import com.art.mq.sdk.support.broadcast.AbstractRedisBroadcastMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;

/**
 * @author fxz
 */
@Slf4j
@RequiredArgsConstructor
public class CacheMessageConsumer extends AbstractRedisBroadcastMessageListener<CacheMessage> {

	private final CacheManager cacheManager;

	@Override
	public void onMessage(CacheMessage cacheMessage) {
		if (cacheManager instanceof RedisCaffeineCacheManager) {
			if (log.isDebugEnabled()) {
				log.debug("接收到redis topic消息，清空本地缓存，缓存名称:{}, key:{}", cacheMessage.getCacheName(),
						cacheMessage.getKey());
			}
			RedisCaffeineCacheManager redisCaffeineCacheManager = (RedisCaffeineCacheManager) cacheManager;
			redisCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());

		}
	}

}