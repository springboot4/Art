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

import com.art.common.canal.support.parser.converter.BigIntCanalFieldConverter;

import java.util.Map;

/**
 * @author fxz
 */
public class BigIntPrimaryKeyTupleFunction extends BasePrimaryKeyTupleFunction {

	/**
	 * 单例
	 */
	public static final BasePrimaryKeyTupleFunction X = new BigIntPrimaryKeyTupleFunction();

	private BigIntPrimaryKeyTupleFunction() {
	}

	@Override
	public Long apply(Map<String, String> before, Map<String, String> after, String primaryKey) {
		String temp;
		if (null != after && null != (temp = after.get(primaryKey))) {
			return BigIntCanalFieldConverter.X.convert(temp);
		}
		return null;
	}

}
