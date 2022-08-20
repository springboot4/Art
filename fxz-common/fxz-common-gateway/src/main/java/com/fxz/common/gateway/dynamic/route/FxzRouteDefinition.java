package com.fxz.common.gateway.dynamic.route;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serializable;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/20 11:53
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class FxzRouteDefinition extends RouteDefinition implements Serializable {

	/**
	 * 路由名称
	 */
	private String name;

}
