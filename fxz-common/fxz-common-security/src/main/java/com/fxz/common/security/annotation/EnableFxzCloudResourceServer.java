package com.fxz.common.security.annotation;

import com.fxz.common.security.config.FxzCloudResourceServerConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fxz 资源服务器注解，通过添加此注解实现配置ResourceServerConfigurerAdapter的效果
 * 资源服务器需要忽略鉴权在配置文件中添加fxz:cloud:security:anonUris 具体配置参考:
 * @see com.fxz.common.security.properties.FxzCloudSecurityProperties
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FxzCloudResourceServerConfigure.class)
public @interface EnableFxzCloudResourceServer {

}
