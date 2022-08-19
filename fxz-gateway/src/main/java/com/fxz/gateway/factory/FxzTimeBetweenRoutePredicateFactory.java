package com.fxz.gateway.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.function.Predicate;

/**
 * 自定义谓词工厂,业务模拟12306 晚上23:00到凌晨6:00不能购票
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/8/19 20:55
 */
@Slf4j
@Component
public class FxzTimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<FxzTimeBetweenConfig> {

	@Override
	public Predicate<ServerWebExchange> apply(FxzTimeBetweenConfig config) {
		LocalTime startTime = config.getStartTime();
		LocalTime endTime = config.getEndTime();

		return (serverWebExchange) -> LocalTime.now().isAfter(startTime) && LocalTime.now().isBefore(endTime);
	}

	public FxzTimeBetweenRoutePredicateFactory() {
		super(FxzTimeBetweenConfig.class);
	}

}
