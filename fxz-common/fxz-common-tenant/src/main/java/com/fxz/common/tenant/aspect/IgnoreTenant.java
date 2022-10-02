package com.fxz.common.tenant.aspect;

import java.lang.annotation.*;

/**
 * 方法执行忽略租户
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 13:42
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IgnoreTenant {

}
