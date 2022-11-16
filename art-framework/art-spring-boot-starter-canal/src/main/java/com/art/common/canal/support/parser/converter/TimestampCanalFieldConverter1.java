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

package com.art.common.canal.support.parser.converter;

import java.sql.JDBCType;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author fxz
 */
public class TimestampCanalFieldConverter1 extends BaseCanalFieldConverter<java.util.Date> {

	public static final BaseCanalFieldConverter<java.util.Date> X = new TimestampCanalFieldConverter1();

	private TimestampCanalFieldConverter1() {
		super(JDBCType.TIMESTAMP, java.util.Date.class);
	}

	@Override
	protected java.util.Date convertInternal(String source) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(source);
		}
		catch (ParseException e) {
			throw new IllegalArgumentException(String.format("转换日期时间类型java.util.Date失败,原始字符串:%s", source), e);
		}
	}

}
