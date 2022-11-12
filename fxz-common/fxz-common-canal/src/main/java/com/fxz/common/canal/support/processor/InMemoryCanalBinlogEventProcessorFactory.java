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

package com.fxz.common.canal.support.processor;

import com.fxz.common.canal.model.ModelTable;
import com.fxz.common.canal.util.AssertUtils;
import com.fxz.common.canal.util.CollectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author fxz
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public class InMemoryCanalBinlogEventProcessorFactory implements CanalBinlogEventProcessorFactory {

	private final ConcurrentMap<ModelTable, List<BaseCanalBinlogEventProcessor<?>>> cache = new ConcurrentHashMap<>(16);

	@Override
	public void register(ModelTable modelTable, BaseCanalBinlogEventProcessor<?> processor) {
		synchronized (cache) {
			cache.putIfAbsent(modelTable, new LinkedList<>());
			cache.get(modelTable).add(processor);
		}
	}

	@Override
	public List<BaseCanalBinlogEventProcessor<?>> get(ModelTable modelTable) {
		List<BaseCanalBinlogEventProcessor<?>> processors = cache.get(modelTable);
		AssertUtils.X.isTrue(CollectionUtils.X.isNotEmpty(processors),
				String.format("Processor Not Found For %s", modelTable));
		return processors;
	}

}
