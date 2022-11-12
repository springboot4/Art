/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fxz.system.configure;

import com.fxz.common.sequence.segment.SeqSegmentConfig;
import com.fxz.common.sequence.segment.SeqSegmentManager;
import com.fxz.common.sequence.service.Sequence;
import com.fxz.common.sequence.service.impl.DefaultSegmentSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/23 14:29
 */
@Configuration
@RequiredArgsConstructor
public class SeqConfig {

	private final SeqSegmentManager seqSegmentManager;

	@Bean
	public Sequence fxzSequence() {
		// 序列号号段配置
		SeqSegmentConfig seqSegmentConfig = new SeqSegmentConfig().setStep(1).setRangeStart(0L).setRangeStep(10);

		return new DefaultSegmentSequence(seqSegmentManager, seqSegmentConfig);
	}

	@Bean
	public Sequence cloudSequence() {
		// 序列号号段配置
		SeqSegmentConfig seqSegmentConfig = new SeqSegmentConfig().setStep(1).setRangeStart(0L).setRangeStep(1000);

		return new DefaultSegmentSequence(seqSegmentManager, seqSegmentConfig);
	}

}
