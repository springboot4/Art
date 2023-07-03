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

package com.art.common.sequence.segment.mysql.service;

import com.art.common.sequence.exception.SeqException;
import com.art.common.sequence.segment.mysql.entity.SequenceSegment;
import com.art.common.sequence.segment.mysql.mapper.SequenceSegmentMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/23 13:17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcSegmentService {

	private final SequenceSegmentMapper mapper;

	/**
	 * 不存在就set，存在就忽略
	 */
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
	public void setIfAbsentRange(String key, Long rangeStart) {
		boolean exists = mapper.exists(Wrappers.<SequenceSegment>lambdaQuery().eq(SequenceSegment::getSegmentKey, key));
		// 是否存在
		if (!exists) {
			mapper.insert(new SequenceSegment().setSegmentKey(key).setSegmentValue(rangeStart));
		}
	}

	/**
	 * 增加号段范围
	 */
	@Transactional(rollbackFor = Exception.class)
	public Long incrementRange(String key, int rangeStep) {
		SequenceSegment segment = mapper
			.selectOne(Wrappers.<SequenceSegment>lambdaQuery().eq(SequenceSegment::getSegmentKey, key));
		if (Objects.isNull(segment)) {
			throw new SeqException("号段不存在");
		}

		// 新号段开始值
		Long stepValue = segment.getSegmentValue() + rangeStep;
		segment.setSegmentValue(stepValue);
		mapper.updateById(segment);

		return stepValue;
	}

}
