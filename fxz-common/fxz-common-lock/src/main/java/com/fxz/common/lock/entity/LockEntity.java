package com.fxz.common.lock.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 17:17
 */
@Data
@Accessors(chain = true)
public class LockEntity {

	/**
	 * 锁名称
	 */
	private String lockName;

	/**
	 * 尝试加锁等待时间
	 */
	private long waitTime = 100;

	/**
	 * 上锁时间
	 */
	private long leaseTime = 200;

	/**
	 * 时间单位,默认为秒
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	/**
	 * 多个key集合
	 */
	private List<String> keyList;

}
