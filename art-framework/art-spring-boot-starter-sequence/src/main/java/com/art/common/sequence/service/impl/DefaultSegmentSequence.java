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

package com.art.common.sequence.service.impl;

import com.art.common.sequence.exception.SeqException;
import com.art.common.sequence.segment.SeqSegment;
import com.art.common.sequence.segment.SeqSegmentConfig;
import com.art.common.sequence.segment.SeqSegmentManager;
import com.art.common.sequence.service.Sequence;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 序列号号段生成器接口默认实现
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/23 10:16
 */
@Setter
@Getter
@RequiredArgsConstructor
public class DefaultSegmentSequence implements Sequence {

	/**
	 * 获取号段加一把重入锁防止资源冲突
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * 序列号号段管理器
	 */
	private final SeqSegmentManager seqSegmentManager;

	/**
	 * 当前序列号号段
	 */
	private volatile SeqSegment currentSegment;

	/**
	 * 序列号号段配置
	 */
	private final SeqSegmentConfig seqSegmentConfig;

	/**
	 * 获取下一个值
	 */
	@Override
	public Long next(String name) throws SeqException {
		// 当前号段不存在，重新获取一个号段
		if (Objects.isNull(currentSegment)) {
			lock.lock();
			try {
				if (Objects.isNull(currentSegment)) {
					currentSegment = seqSegmentManager.nextSegment(name, seqSegmentConfig);
				}
			}
			finally {
				lock.unlock();
			}
		}

		// 当value值为-1时，表明号段的序列号已经分配完，需要重新获取号段
		Long value = currentSegment.getAndIncrement();
		if (value == -1) {
			lock.lock();
			try {
				for (;;) {
					if (currentSegment.getOver()) {
						currentSegment = seqSegmentManager.nextSegment(name, seqSegmentConfig);
					}
					value = currentSegment.getAndIncrement();
					if (value == -1) {
						continue;
					}
					break;
				}
			}
			finally {
				lock.unlock();
			}
		}
		if (value < 0) {
			throw new SeqException("序列值溢出! value:" + value);
		}
		return value;
	}

	/**
	 * 下一个生成序号（带格式）
	 */
	@Override
	public String nextValue(String name) throws SeqException {
		return String.valueOf(this.next(name));
	}

}
