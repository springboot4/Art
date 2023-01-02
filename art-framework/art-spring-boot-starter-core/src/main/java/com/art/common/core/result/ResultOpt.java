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

package com.art.common.core.result;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/1/2 17:06
 */
public class ResultOpt<T> {

	/**
	 * 一个空的{@code ResultOpt}
	 */
	private static final ResultOpt<?> EMPTY = new ResultOpt<>(null);

	/**
	 * 包裹的实际的元素
	 */
	private final Result<T> result;

	/**
	 * 构造方法
	 * @param result 包裹里的元素
	 */
	private ResultOpt(Result<T> result) {
		this.result = result;
	}

	/**
	 * 返回一个包裹里元素不可能为空的{@code ResultOpt}
	 * @param result 包裹里的元素
	 * @param <T> 包裹里元素的类型
	 * @return 一个包裹里元素不可能为空的 {@code ResultOpt}
	 * @throws java.lang.NullPointerException 如果传入的元素为空，抛出 {@code NPE}
	 */
	public static <T> ResultOpt<T> of(Result<T> result) {
		return new ResultOpt<>(Objects.requireNonNull(result));
	}

	/**
	 * 返回一个包裹里元素可能为空的{@code ResultOpt}
	 * @param result 传入需要包裹的元素
	 * @param <T> 包裹里元素的类型
	 * @return 一个包裹里元素可能为空的 {@code ResultOpt}
	 */
	public static <T> ResultOpt<T> ofNullable(Result<T> result) {
		return result == null ? empty() : new ResultOpt<>(result);
	}

	/**
	 * 返回一个空的{@code ResultOpt}
	 * @param <T> 包裹里元素的类型
	 * @return Opp
	 */
	public static <T> ResultOpt<T> empty() {
		@SuppressWarnings("unchecked")
		final ResultOpt<T> t = (ResultOpt<T>) EMPTY;
		return t;
	}

	/**
	 * 如果包裹里元素的值存在，就执行对应的操作，并返回本身 如果不存在，返回null{@code null}
	 * @param action 值存在时执行的操作
	 * @return result
	 * @throws java.lang.NullPointerException 如果action不存在
	 */
	public Result<T> peek(Consumer<Result<T>> action) throws NullPointerException {
		Objects.requireNonNull(action);
		if (!isEmpty()) {
			action.accept(result);
		}

		return peek();
	}

	/**
	 * 获取result
	 * @return result
	 */
	public Result<T> peek() {
		return result;
	}

	/**
	 * 读取{@code code}的值
	 * @return 返回code的值
	 */
	public String getCode() {
		return result.getCode();
	}

	/**
	 * 读取{@code data}的值
	 * @return 返回 Optional 包装的data
	 */
	public Optional<T> getData() {
		return Optional.ofNullable(result.getData());
	}

	/**
	 * 读取{@code msg}的值
	 * @return 返回Optional包装的 msg
	 */
	public Optional<String> getMsg() {
		return Optional.ofNullable(result.getMsg());
	}

	/**
	 * 有条件地读取{@code data}的值
	 * @param predicate 断言函数
	 * @return 返回 Optional 包装的data,如果断言失败返回empty
	 */
	public Optional<T> filter(Predicate<? super Result<?>> predicate) {
		return predicate.test(result) ? getData() : Optional.empty();
	}

	/**
	 * 判断包裹里元素的值是否不存在，不存在为 {@code true}，否则为{@code false}
	 * @return 包裹里元素的值不存在 则为 {@code true}，否则为{@code false}
	 */
	public boolean isEmpty() {
		return result == null;
	}

	/**
	 * 对{@code code}的值进行相等性测试
	 * @param code 基准值
	 * @return 返回ture表示相等
	 */
	public boolean codeEquals(String code) {
		return result.getCode().equals(code);
	}

	/**
	 * 对{@code code}的值进行相等性测试
	 * @param code 基准值
	 * @return 返回ture表示不相等
	 */
	public boolean codeNotEquals(String code) {
		return !codeEquals(code);
	}

	/**
	 * 是否成功
	 * @return 返回ture表示成功
	 */
	public boolean isSuccess() {
		return codeEquals(ResultCode.SUCCESS.getCode());
	}

	/**
	 * 是否失败
	 * @return 返回ture表示失败
	 */
	public boolean notSuccess() {
		return !isSuccess();
	}

	/**
	 * 断言{@code code}的值
	 * @param code 预期的值
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 * @throws Ex 断言失败时抛出
	 */
	public <Ex extends Exception> ResultOpt<T> assertCode(String code, Function<? super Result<T>, ? extends Ex> func)
			throws Ex {
		if (codeNotEquals(code)) {
			throw func.apply(result);
		}

		return this;
	}

	/**
	 * 断言成功
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 * @throws Ex 断言失败时抛出
	 */
	public <Ex extends Exception> ResultOpt<T> assertSuccess(Function<? super Result<T>, ? extends Ex> func) throws Ex {
		return assertCode(ResultCode.SUCCESS.getCode(), func);
	}

	/**
	 * 断言业务数据有值
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 * @throws Ex 断言失败时抛出
	 */
	public <Ex extends Exception> ResultOpt<T> assertDataNotNull(Function<? super Result<T>, ? extends Ex> func)
			throws Ex {
		if (Objects.isNull(result.getData())) {
			throw func.apply(result);
		}

		return this;
	}

	/**
	 * 断言业务数据有值,并且包含元素
	 * @param func 用户函数,负责创建异常对象
	 * @param <Ex> 异常类型
	 * @return 返回实例，以便于继续进行链式操作
	 * @throws Ex 断言失败时抛出
	 */
	public <Ex extends Exception> ResultOpt<T> assertDataNotEmpty(Function<? super Result<T>, ? extends Ex> func)
			throws Ex {
		if (ObjectUtil.isEmpty(result.getData())) {
			throw func.apply(result);
		}

		return this;
	}

	/**
	 * 对业务数据(data)转换
	 * @param mapper 业务数据转换函数
	 * @param <U> 数据类型
	 * @return 返回新实例，以便于继续进行链式操作
	 */
	public <U> ResultOpt<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);

		if (isEmpty()) {
			return empty();
		}
		else {
			return of(Result.result(result.getCode(), result.getMsg(), mapper.apply(result.getData())));
		}
	}

	/**
	 * 对业务数据(data)转换
	 * @param predicate 断言函数
	 * @param mapper 业务数据转换函数
	 * @param <U> 数据类型
	 * @return 返回新实例
	 */
	public <U> ResultOpt<U> mapIf(Predicate<? super Result<T>> predicate, Function<? super T, ? extends U> mapper) {
		if (predicate.test(result)) {
			return empty();
		}
		else {
			return of(Result.result(result.getCode(), result.getMsg(), mapper.apply(result.getData())));
		}
	}

	/**
	 * 消费数据,注意此方法保证数据可用
	 * @param consumer 消费函数
	 */
	public ResultOpt<T> consumerData(Consumer<? super T> consumer) {
		consumer.accept(result.getData());
		return this;
	}

	/**
	 * 条件消费(期望code匹配某个值)
	 * @param consumer 消费函数
	 * @param codes 期望code集合 匹配任意一个则调用消费函数
	 */
	public ResultOpt<T> consumerDataOnCode(Consumer<? super T> consumer, String... codes) {
		consumerDataIf(o -> Arrays.stream(codes).anyMatch(c -> result.getCode().equals(c)), consumer);
		return this;
	}

	/**
	 * 条件消费
	 * @param consumer 消费函数
	 */
	public ResultOpt<T> consumerDataIfSuccess(Consumer<? super T> consumer) {
		Predicate<Result<T>> success = r -> ResultCode.SUCCESS.getCode().equals(r.getCode());
		consumerDataIf(success, consumer);
		return this;
	}

	/**
	 * 条件消费
	 * @param predicate 断言函数
	 * @param consumer 消费函数,断言函数返回{@code true}时被调用
	 */
	public ResultOpt<T> consumerDataIf(Predicate<? super Result<T>> predicate, Consumer<? super T> consumer) {
		if (predicate.test(result)) {
			consumer.accept(result.getData());
		}

		return this;
	}

}
