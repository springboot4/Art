package com.fxz.common.security.constant;

/**
 * 端点常量
 *
 * @author fxz
 */
public interface EndpointConstant {

	String ALL = "/**";

	String OAUTH_ALL = "/oauth/**";

	String OAUTH_AUTHORIZE = "/oauth/authorize";

	String OAUTH_CHECK_TOKEN = "/oauth/check_token";

	String OAUTH_CONFIRM_ACCESS = "/oauth/confirm_access";

	String OAUTH_TOKEN = "/oauth/token";

	String OAUTH_TOKEN_KEY = "/oauth/token_key";

	String OAUTH_ERROR = "/oauth/error";

	String ACTUATOR_ALL = "/actuator/**";

	String LOGIN = "/login";

}
