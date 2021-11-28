package com.common.annotation;

import com.common.configure.FxzAuthExceptionConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 认证类型异常翻译
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 21:57
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FxzAuthExceptionConfigure.class)
public @interface EnableFxzAuthExceptionHandler {
}
