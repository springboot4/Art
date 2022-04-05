package com.fxz.common.core.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.mp.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 13:12
 */
public class FxzServerProtectInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		if (1 > 0)
			return true;
		String path = request.getRequestURI();
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		boolean match = antPathMatcher.match("/token/**", path);
		if (match) {
			return true;
		}

		// 从请求头中获取 gateway Token
		String token = request.getHeader(FxzConstant.GATEWAY_TOKEN_HEADER);
		String gatewayToken = new String(Base64Utils.encode(FxzConstant.GATEWAY_TOKEN_VALUE.getBytes()));
		// 校验 gateway Token的正确性
		if (StringUtils.equals(gatewayToken, token)) {
			return true;
		}
		else {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write(JSONObject.toJSONString(Result.failed("Obtain resources through the gateway!")));
			return false;
		}
	}

}
