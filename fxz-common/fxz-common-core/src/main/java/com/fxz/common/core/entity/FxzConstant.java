package com.fxz.common.core.entity;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 13:10
 */
public class FxzConstant {

	/**
	 * GATEWAY请求头TOKEN名称（不要有空格）
	 */
	public static final String GATEWAY_TOKEN_HEADER = "GatewayToken";

	/**
	 * GATEWAY请求头TOKEN值
	 */
	public static final String GATEWAY_TOKEN_VALUE = "fxz:gateway:$**$";

	/**
	 * gif类型
	 */
	public static final String GIF = "gif";

	/**
	 * png类型
	 */
	public static final String PNG = "png";

	/**
	 * 验证码 key前缀
	 */
	public static final String CODE_PREFIX = "fxz.captcha.";

	public static final String OAUTH2_TOKEN_TYPE = "bearer ";

}
