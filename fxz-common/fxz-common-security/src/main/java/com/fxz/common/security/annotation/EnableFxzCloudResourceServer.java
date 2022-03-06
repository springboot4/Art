package com.fxz.common.security.annotation;

import com.fxz.common.security.confign.FxzCloudResourceServerConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fxz
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FxzCloudResourceServerConfigure.class)
public @interface EnableFxzCloudResourceServer {

}
