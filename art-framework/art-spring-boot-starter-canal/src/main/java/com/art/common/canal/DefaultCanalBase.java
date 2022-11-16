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

package com.art.common.canal;

import com.art.common.canal.model.CanalBinLogEvent;
import com.art.common.canal.model.ModelTable;
import com.art.common.canal.support.adapter.SourceAdapterFacade;
import com.art.common.canal.support.processor.CanalBinlogEventProcessorFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @author fxz
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public class DefaultCanalBase implements CanalBase {

	private final CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory;

	@Override
	public void process(String content) {
		CanalBinLogEvent event = SourceAdapterFacade.X.adapt(CanalBinLogEvent.class, content);
		ModelTable modelTable = ModelTable.of(event.getDatabase(), event.getTable());
		canalBinlogEventProcessorFactory.get(modelTable).forEach(processor -> processor.process(event));
	}

}
