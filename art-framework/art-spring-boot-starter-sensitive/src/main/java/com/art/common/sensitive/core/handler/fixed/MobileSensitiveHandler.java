package com.art.common.sensitive.core.handler.fixed;

import com.art.common.sensitive.core.annotation.fixed.MobileSensitive;

/**
 * @author fxz
 */
public class MobileSensitiveHandler extends AbstractFixedSensitiveHandler<MobileSensitive> {

	@Override
	Integer getFront(MobileSensitive annotation) {
		return annotation.front();
	}

	@Override
	Integer getEnd(MobileSensitive annotation) {
		return annotation.end();
	}

	@Override
	String getReplacer(MobileSensitive annotation) {
		return annotation.replacer();
	}

}
