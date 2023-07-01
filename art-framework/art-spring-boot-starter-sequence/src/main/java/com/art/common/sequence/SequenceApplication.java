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

package com.art.common.sequence;

import com.art.common.sequence.config.SequenceProperties;
import com.art.common.sequence.segment.SeqSegmentConfig;
import com.art.common.sequence.segment.SeqSegmentManager;
import com.art.common.sequence.service.Sequence;
import com.art.common.sequence.service.impl.DefaultSegmentSequence;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/22 20:45
 */
@ComponentScan
@RequiredArgsConstructor
@MapperScan(annotationClass = Mapper.class)
@EnableConfigurationProperties(SequenceProperties.class)
public class SequenceApplication {

	private final SequenceProperties sequenceProperties;

	@Bean
	@ConditionalOnMissingBean(Sequence.class)
	public Sequence sequence(SeqSegmentManager seqSegmentManager) {
		// 序列号号段配置
		SeqSegmentConfig seqSegmentConfig = new SeqSegmentConfig().setStep(sequenceProperties.getStep())
			.setRangeStart(sequenceProperties.getRangeStart())
			.setRangeStep(sequenceProperties.getRangeStep());

		// 序列号号段生成器接口默认实现
		return new DefaultSegmentSequence(seqSegmentManager, seqSegmentConfig);
	}

}
