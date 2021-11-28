package com.fxz.serversystem.service.fallback;

import com.fxz.serversystem.service.IHelloService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 12:31
 */
@Slf4j
public class HelloServiceFallback implements FallbackFactory<IHelloService> {

    @Override
    public IHelloService create(Throwable throwable) {
        return (name) -> {
            log.error("调用fxz-server-system服务出错", throwable);
            return "调用出错";

        };
    }

}
