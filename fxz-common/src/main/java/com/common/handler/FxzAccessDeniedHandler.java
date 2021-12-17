package com.common.handler;

import com.common.entity.FxzResponse;
import com.common.utils.FxzUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于处理403类型异常
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 21:52
 */
public class FxzAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {
		FxzResponse fxzResponse = new FxzResponse();
		FxzUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_FORBIDDEN,
				fxzResponse.message("没有权限访问该资源"));
	}

}
