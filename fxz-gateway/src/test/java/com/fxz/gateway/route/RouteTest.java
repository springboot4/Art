package com.fxz.gateway.route;

import com.fxz.common.gateway.dynamic.route.FxzRouteDefinition;
import com.fxz.common.gateway.dynamic.route.FxzRouteDefinitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/20 12:58
 */
@Slf4j
@SpringBootTest
public class RouteTest {

	@Autowired
	private FxzRouteDefinitionRepository repository;

	@Test
	void save() {
		repository.save(Mono.just(newRoute()));
		this.publishEvent();
	}

	@Test
	void delete() {
		repository.delete(Mono.just("fxz-server-system")).block();
		this.publishEvent();
	}

	@Test
	void saveAll() {
	}

	@Test
	void findAll() {
	}

	@Test
	void deleteAll() {
	}

	@Test
	void publishEvent() {
		repository.publishEvent();
	}

	private RouteDefinition newRoute() {
		FxzRouteDefinition route = new FxzRouteDefinition();
		route.setName("system模块");
		route.setId("fxz-server-system");
		PredicateDefinition predicate = new PredicateDefinition("Path=/system/**");
		route.setPredicates(Collections.singletonList(predicate));
		route.setOrder(1);
		route.setUri(URI.create("lb://fxz-server-system"));
		return route;
	}

}
