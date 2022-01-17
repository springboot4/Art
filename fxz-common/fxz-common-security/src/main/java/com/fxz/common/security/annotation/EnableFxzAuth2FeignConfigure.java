package com.fxz.common.security.annotation;

import com.fxz.common.security.confign.FxzOAuth2FeignConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启带令牌的Feign请求，避免微服务内部调用出现401异常
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 12:47
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FxzOAuth2FeignConfigure.class)
public @interface EnableFxzAuth2FeignConfigure {

}
