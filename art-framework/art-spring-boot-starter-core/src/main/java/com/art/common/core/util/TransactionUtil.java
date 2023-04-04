/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.core.util;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;
import java.util.function.Supplier;

public class TransactionUtil {

	/**
	 * 传播行为：支持当前事务，如果不存在，则创建一个新事务<br>
	 * 隔离级别：读已提交
	 * @param runnable 方法执行逻辑
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void runWithRequiresReadCommit(Runnable runnable) {
		runnable.run();
	}

	/**
	 * 传播行为：支持当前事务，如果不存在，则创建一个新事务<br>
	 * 隔离级别：读已提交
	 * @param supplier 方法执行逻辑
	 * @param <T> 返回值类型
	 * @return T类型返回值
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public <T> T getWithRequiresReadCommit(Supplier<T> supplier) {
		return supplier.get();
	}

	/**
	 * 传播行为：支持当前事务，如果不存在，则创建一个新事务<br>
	 * 隔离级别：读已提交
	 * @param func 方法执行逻辑
	 * @param t 方法入参
	 * @param <T> 方法入参类型
	 * @param <R> 方法返回值类型
	 * @return R类型返回值
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public <T, R> R functionWithRequiresReadCommit(Function<T, R> func, T t) {
		return func.apply(t);
	}

	/**
	 * 传播行为：创建一个新事务，并挂起当前事务（如果存在)<br>
	 * 隔离级别：读已提交
	 * @param runnable 方法执行逻辑
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
	public void runWithRequiresNewReadCommit(Runnable runnable) {
		runnable.run();
	}

	/**
	 * 传播行为：创建一个新事务，并挂起当前事务（如果存在)<br>
	 * 隔离级别：读已提交
	 * @param supplier 方法执行逻辑
	 * @param <T> 返回值类型
	 * @return T类型返回值
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
	public <T> T getWithRequiresNewReadCommit(Supplier<T> supplier) {
		return supplier.get();
	}

	/**
	 * 传播行为：创建一个新事务，并挂起当前事务（如果存在)<br>
	 * 隔离级别：读已提交
	 * @param func 方法执行逻辑
	 * @param t 入参
	 * @param <T> 入参类型
	 * @param <R> 返回值类型
	 * @return R类型返回值
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
	public <T, R> R functionWithRequiresNewReadCommit(Function<T, R> func, T t) {
		return func.apply(t);
	}

}
