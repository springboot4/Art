package com.fxz.common.redis.constant;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/20 12:06
 */
public interface CacheConstants {

	/**
	 * 全局缓存标识
	 */
	String GLOBALLY = "fxz_cloud:";

	/**
	 * 网关路由
	 */
	String ROUTE_KEY = GLOBALLY + "gateway_route_key";

}
