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

import java.sql.JDBCType;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author fxz
 */
public class TimestampCanalFieldConverter2 extends BaseCanalFieldConverter<OffsetDateTime> {

	public static final BaseCanalFieldConverter<OffsetDateTime> X = new TimestampCanalFieldConverter2();

	/**
	 * 东八区
	 */
	private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+08:00");

	private TimestampCanalFieldConverter2() {
		super(JDBCType.TIMESTAMP, OffsetDateTime.class);
	}

	@Override
	protected OffsetDateTime convertInternal(String source) {
		LocalDateTime localDateTime = TimestampCanalFieldConverter0.X.convert(source);
		return OffsetDateTime.of(localDateTime, ZONE_OFFSET);
	}

}
