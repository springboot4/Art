/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
