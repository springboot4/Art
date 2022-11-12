package com.fxz.common.lock.entity;

import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/9/4 17:17
 */
@Data
public class LockEntity {

	/**
	 * 锁名称，适用于公平锁、读写锁、重入锁
	 */
	private String lockName;

	/**
	 * 尝试加锁等待时间
	 */
	private long waitTime = 60L;

	/**
	 * 上锁时间
	 */
	private long leaseTime = 60L;

	/**
	 * 时间单位,默认为秒
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	/**
	 * 多个key集合,适用于连锁、红锁
	 */
	private List<String> keyList;

}
