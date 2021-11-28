package com.fxz.serversystem.service;

import com.common.entity.FxzServerConstant;
import com.fxz.serversystem.service.fallback.HelloServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 12:23
 */
@FeignClient(value = FxzServerConstant.FXZ_SERVER_SYSTEM, contextId = "helloServiceClient", fallbackFactory = HelloServiceFallback.class)
public interface IHelloService {

    @GetMapping("/hello")
    String hello(@RequestParam("name") String name);

}
