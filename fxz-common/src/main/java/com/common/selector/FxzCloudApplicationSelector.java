package com.common.selector;

import com.common.configure.FxzAuthExceptionConfigure;
import com.common.configure.FxzOAuth2FeignConfigure;
import com.common.configure.FxzServerProtectConfigure;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 13:34
 */
public class FxzCloudApplicationSelector implements ImportSelector {

    @SuppressWarnings("all")
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                FxzAuthExceptionConfigure.class.getName(),
                FxzOAuth2FeignConfigure.class.getName(),
                FxzServerProtectConfigure.class.getName()
        };
    }

}
