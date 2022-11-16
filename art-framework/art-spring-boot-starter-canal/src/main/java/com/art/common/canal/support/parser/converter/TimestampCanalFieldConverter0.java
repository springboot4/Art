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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fxz
 */
public class TimestampCanalFieldConverter0 extends BaseCanalFieldConverter<LocalDateTime> {

	private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static final BaseCanalFieldConverter<LocalDateTime> X = new TimestampCanalFieldConverter0();

	private TimestampCanalFieldConverter0() {
		super(JDBCType.TIMESTAMP, LocalDateTime.class);
	}

	@Override
	protected LocalDateTime convertInternal(String source) {
		return LocalDateTime.parse(source, F);
	}

}
