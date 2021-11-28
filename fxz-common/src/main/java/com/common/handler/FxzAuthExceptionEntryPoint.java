package com.common.handler;

import com.common.entity.FxzResponse;
import com.common.utils.FxzUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源服务器异常主要有两种：令牌不正确返回401和用户无权限返回403。
 * 因为资源服务器有多个，所以相关的异常处理类可以定义在fxz-common通用模块里。
 * <p>
 * 用于处理401类型异常
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 21:38
 */
public class FxzAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        FxzResponse fxzResponse = new FxzResponse();

        FxzUtil.makeResponse(response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                HttpServletResponse.SC_UNAUTHORIZED, fxzResponse.message("token无效")
        );
    }

}
