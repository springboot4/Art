package com.fxz.common.gateway.dynamic.annotation;

import com.fxz.common.gateway.dynamic.config.CustomGatewayAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启动态路由
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/8/20 14:34
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CustomGatewayAutoConfiguration.class)
public @interface EnableDynamicRoute {

}
