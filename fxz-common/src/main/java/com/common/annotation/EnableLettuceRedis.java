package com.common.annotation;

import com.common.configure.FxzLettuceRedisConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 16:42
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FxzLettuceRedisConfigure.class)
public @interface EnableLettuceRedis {
}
