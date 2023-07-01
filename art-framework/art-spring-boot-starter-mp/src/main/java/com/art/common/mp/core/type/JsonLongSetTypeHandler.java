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

package com.art.common.mp.core.type;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.art.common.jackson.util.JacksonUtil;

import java.util.Set;

/**
 * @author fxz
 */
public class JsonLongSetTypeHandler extends AbstractJsonTypeHandler<Object> {

	private static final TypeReference<Set<Long>> TYPE_REFERENCE = new TypeReference<Set<Long>>() {
	};

	@Override
	protected Object parse(String json) {
		return JacksonUtil.parseObject(json, TYPE_REFERENCE);
	}

	@Override
	protected String toJson(Object obj) {
		return JacksonUtil.toJsonString(obj);
	}

}