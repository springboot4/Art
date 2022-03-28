package com.fxz.common.security.annotation;

import com.fxz.common.security.config.FxzServerProtectConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启微服务防护，避免客户端绕过网关直接请求微服务
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 13:26
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FxzServerProtectConfigure.class)
public @interface EnableFxzServerProtect {

}
