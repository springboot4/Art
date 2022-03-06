package com.fxz.common.core.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fxz.common.core.entity.FxzConstant;
import com.fxz.common.core.entity.FxzResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
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
		// 从请求头中获取 GATEWAY Token
		String token = request.getHeader(FxzConstant.GATEWAY_TOKEN_HEADER);
		String gatewayToken = new String(Base64Utils.encode(FxzConstant.GATEWAY_TOKEN_VALUE.getBytes()));
		// 校验 GATEWAY Token的正确性
		if (StringUtils.equals(gatewayToken, token)) {
			return true;
		}
		else {
			FxzResponse fxzResponse = new FxzResponse();
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter()
					.write(JSONObject.toJSONString(fxzResponse.message("Obtain resources through the gateway!")));
			return false;
		}
	}

}
