package com.fxz.gateway.filter;

import com.common.entity.FxzResponse;
import com.common.utils.FxzUtil;
import com.netflix.zuul.context.RequestContext;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 22:09
 */
@Slf4j
@Component
public class FxzGatewayErrorFilter extends SendErrorFilter {
    @Override
    public Object run() {
        try {
            FxzResponse fxzResponse = new FxzResponse();
            RequestContext ctx = RequestContext.getCurrentContext();
            String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);

            ExceptionHolder exception = findZuulException(ctx.getThrowable());
            String errorCause = exception.getErrorCause();
            Throwable throwable = exception.getThrowable();
            String message = throwable.getMessage();
            message = StringUtils.isBlank(message) ? errorCause : message;
            fxzResponse = resolveExceptionMessage(message, serviceId, fxzResponse);

            HttpServletResponse response = ctx.getResponse();
            FxzUtil.makeResponse(
                    response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fxzResponse
            );
            log.error("Zull sendError：{}", fxzResponse.getMessage());
        } catch (Exception ex) {
            log.error("Zuul sendError", ex);
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    private FxzResponse resolveExceptionMessage(String message, String serviceId, FxzResponse fxzResponse) {
        if (StringUtils.containsIgnoreCase(message, "time out")) {
            return fxzResponse.message("请求" + serviceId + "服务超时");
        }
        if (StringUtils.containsIgnoreCase(message, "forwarding error")) {
            return fxzResponse.message(serviceId + "服务不可用");
        }
        return fxzResponse.message("Zuul请求" + serviceId + "服务异常");
    }
}
