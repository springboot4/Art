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

package com.fxz.common.canal.support.parser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author fxz
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public class InMemoryParseResultInterceptorManager implements ParseResultInterceptorManager {

	private final ConcurrentMap<Class<?>, List<BaseParseResultInterceptor<?>>> cache = new ConcurrentHashMap<>(16);

	private final ModelTableMetadataManager modelTableMetadataManager;

	@Override
	public <T> void registerParseResultInterceptor(BaseParseResultInterceptor<T> parseResultInterceptor) {
		synchronized (cache) {
			Class<T> klass = parseResultInterceptor.getKlass();
			ModelTableMetadata modelTableMetadata = modelTableMetadataManager.load(klass);
			Optional.ofNullable(modelTableMetadata).ifPresent(ignore -> {
				cache.putIfAbsent(parseResultInterceptor.getKlass(), new LinkedList<>());
				cache.get(klass).add(parseResultInterceptor);
			});
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<BaseParseResultInterceptor<T>> getParseResultInterceptors(Class<T> klass) {
		return (List<BaseParseResultInterceptor<T>>) (List<?>) cache.getOrDefault(klass, Collections.emptyList());
	}

}
