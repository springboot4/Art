package com.fxz.common.security.annotation;

import java.lang.annotation.*;

/**
 * 资源服务器接口、类通过添加此注解达到忽略鉴权的作用
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-03-08 19:55
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ojbk {

}
