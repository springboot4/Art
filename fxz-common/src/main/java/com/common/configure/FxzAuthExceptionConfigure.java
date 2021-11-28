package com.common.configure;

import com.common.handler.FxzAccessDeniedHandler;
import com.common.handler.FxzAuthExceptionEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * common模块是一个普通的maven项目，并不是一个Spring Boot项目，所以即使在这两个类上使用@Component注解标注，
 * 它们也不能被成功注册到各个微服务子系统的Spring IOC容器中。我们可以使用@Enable模块驱动的方式来解决这个问题。
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 21:55
 */
public class FxzAuthExceptionConfigure {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public FxzAccessDeniedHandler accessDeniedHandler() {
        return new FxzAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public FxzAuthExceptionEntryPoint authenticationEntryPoint() {
        return new FxzAuthExceptionEntryPoint();
    }

}
