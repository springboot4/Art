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

package com.art.common.canal.support.parser;

import com.art.common.canal.model.CanalBinLogEvent;
import com.art.common.canal.model.CanalBinLogResult;

import java.util.List;

/**
 * @author fxz
 */
public interface CanalBinLogEventParser {

	/**
	 * 解析binlog事件
	 * @param event 事件，这个是上游的适配器组件的结果。
	 * @param klass 目标类型
	 * @param primaryKeyFunction 主键映射方法
	 * @param commonEntryFunction 非主键通用列-属性映射方法实例
	 * @return CanalBinLogResult
	 */
	<T> List<CanalBinLogResult<T>> parse(CanalBinLogEvent event, Class<T> klass,
			BasePrimaryKeyTupleFunction primaryKeyFunction, BaseCommonEntryFunction<T> commonEntryFunction);

}
