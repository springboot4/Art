package com.fxz.gateway.configure;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-12-07 11:36
 */
@Configuration
public class FxzGatewaySentinelConfigure {

	private final List<ViewResolver> viewResolvers;

	private final ServerCodecConfigurer serverCodecConfigurer;

	public FxzGatewaySentinelConfigure(ObjectProvider<List<ViewResolver>> viewResolversProvider,
			ServerCodecConfigurer serverCodecConfigurer) {
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
		return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
	}

	@Bean
	@Order(-1)
	public GlobalFilter sentinelGatewayFilter() {
		return new SentinelGatewayFilter();
	}

	@PostConstruct
	public void doInit() {
		initGatewayRules();
	}

	private void initGatewayRules() {
		Set<ApiDefinition> definitions = new HashSet<>();
		Set<ApiPredicateItem> predicateItems = new HashSet<>();

		predicateItems.add(new ApiPathPredicateItem().setPattern("/auth/captcha"));
		ApiDefinition definition = new ApiDefinition("captcha").setPredicateItems(predicateItems);
		definitions.add(definition);
		GatewayApiDefinitionManager.loadApiDefinitions(definitions);

		Set<GatewayFlowRule> rules = new HashSet<>();

		rules.add(new GatewayFlowRule("captcha").setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
				.setParamItem(new GatewayParamFlowItem()
						.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM).setFieldName("key")
						.setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_EXACT)
						.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP))
				.setCount(10).setIntervalSec(60));
		GatewayRuleManager.loadRules(rules);
	}

}
