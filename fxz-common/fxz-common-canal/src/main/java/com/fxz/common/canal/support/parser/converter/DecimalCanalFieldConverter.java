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

package com.fxz.common.canal.support.parser.converter;

import java.math.BigDecimal;
import java.sql.JDBCType;

/**
 * @author fxz
 */
public class DecimalCanalFieldConverter extends BaseCanalFieldConverter<BigDecimal> {

	/**
	 * 单例
	 */
	public static final BaseCanalFieldConverter<BigDecimal> X = new DecimalCanalFieldConverter();

	private DecimalCanalFieldConverter() {
		super(JDBCType.DECIMAL, BigDecimal.class);
	}

	@Override
	protected BigDecimal convertInternal(String source) {
		return new BigDecimal(source);
	}

}
