package com.fxz.common.mp.encrypt.annotation;

import java.lang.annotation.*;

/**
 * 数据加密注解
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/15 14:50
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EncryptionData {

}
