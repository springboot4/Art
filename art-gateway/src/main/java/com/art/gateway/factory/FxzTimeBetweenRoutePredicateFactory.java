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

package com.art.gateway.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义谓词工厂,业务模拟12306 晚上23:00到凌晨6:00不能购票
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/8/19 20:55
 */
@Slf4j
@Component
public class FxzTimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<FxzTimeBetweenConfig> {

	@Override
	public Predicate<ServerWebExchange> apply(FxzTimeBetweenConfig config) {
		return (serverWebExchange) -> LocalTime.now().isAfter(config.getStartTime())
				&& LocalTime.now().isBefore(config.getEndTime());
	}

	public List<String> shortcutFieldOrder() {
		return Arrays.asList(FxzTimeBetweenConfig.Fields.startTime, FxzTimeBetweenConfig.Fields.endTime);
	}

	public FxzTimeBetweenRoutePredicateFactory() {
		super(FxzTimeBetweenConfig.class);
	}

}
