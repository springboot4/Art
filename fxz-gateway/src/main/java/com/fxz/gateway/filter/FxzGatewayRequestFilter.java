package com.fxz.gateway.filter;

import com.common.entity.FxzConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 13:06
 */
@Slf4j
@Component
public class FxzGatewayRequestFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * PreDecorationFilter过滤器的优先级为5，所以我们可以指定为6让我们的过滤器优先级比它低；
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        HttpServletRequest request = ctx.getRequest();
        String host = request.getRemoteHost();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.info("请求URI：{}，HTTP Method：{}，请求IP：{}，ServerId：{}", uri, method, host, serviceId);

        byte[] token = Base64Utils.encode((FxzConstant.ZUUL_TOKEN_VALUE).getBytes());
        ctx.addZuulRequestHeader(FxzConstant.ZUUL_TOKEN_HEADER, new String(token));
        return null;
    }

}
