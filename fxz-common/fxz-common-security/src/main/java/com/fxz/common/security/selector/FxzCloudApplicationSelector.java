package com.fxz.common.security.selector;

import com.fxz.common.security.confign.FxzOAuth2FeignConfigure;
import com.fxz.common.security.confign.FxzServerProtectConfigure;
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
		return new String[] { FxzOAuth2FeignConfigure.class.getName(), FxzServerProtectConfigure.class.getName() };
	}

}
