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

package com.art.demos.core.java;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/18 17:41
 */
public class Lambda {

	/**
	 * 拼接集合元素 {@code Lambda.join(list,",",User::getUserId)}
	 * @param originList 原始集合
	 * @param delimiter 拼接分隔符
	 * @param mapper Function
	 * @return {@link String}
	 */
	public static <T> String join(List<T> originList, String delimiter, Function<T, String> mapper) {
		// 空集合不处理
		if (CollUtil.isEmpty(originList)) {
			return null;
		}

		// 删除集合中的空元素
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return null;
		}

		// 对集合元素进行拼接
		return originList.stream().map(mapper).filter(StrUtil::isNotBlank).collect(Collectors.joining(delimiter));
	}

	/**
	 * 集合过滤
	 * @param originList 原数据
	 * @param mappers 规则集合
	 * @param <T> 原数据的元素类型
	 * @return List<T> 过滤后的集合
	 */
	@SafeVarargs
	public static <T> List<T> filterToList(List<T> originList, Predicate<T>... mappers) {
		// 空集合不处理
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		// 删除空元素
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		// 拼接所有条件 进行过滤
		return Arrays.stream(mappers)
			.reduce(Predicate::and)
			.map(originList.stream()::filter)
			.orElse(Stream.empty())
			.collect(Collectors.toList());
	}

	/**
	 * 集合过滤同时去重
	 * @param originList 原数据
	 * @param mappers 规则集合
	 * @param <T> 原数据的元素类型
	 * @return List<T>
	 */
	@SafeVarargs
	public static <T> List<T> filterToDistinctList(List<T> originList, Predicate<T>... mappers) {
		// 空集合不处理
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		// 删除空元素
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		// 按照所有规则过滤并去重
		return originList.stream()
			.filter(Arrays.stream(mappers).reduce(Predicate::and).orElse(t -> true))
			.distinct()
			.collect(Collectors.toList());
	}

	/**
	 * list集合中符合条件的映射成map
	 * @param originList 原数据
	 * @param mappers 映射规则集合
	 * @param <T> 原数据的元素类型
	 * @return List<T>
	 */
	@SafeVarargs
	public static <T, R> List<R> filtersToMapList(List<T> originList, Function<T, R> function,
			Predicate<T>... mappers) {
		// 空集合不处理
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		// 删除空元素
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		// list集合中符合条件的映射成map
		return originList.stream()
			.filter(Stream.of(mappers).reduce(Predicate::and).orElse(t -> true))
			.map(function)
			.collect(Collectors.toList());
	}

	/**
	 * @param originList 原数据
	 * @param mappers 映射规则集合
	 * @param <T> 原数据的元素类型
	 * @return List<T>
	 */
	@SafeVarargs
	public static <T, R> List<R> filtersMapToDistinctList(List<T> originList, Function<T, R> function,
			Predicate<T>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		return originList.stream()
			.filter(Stream.of(mappers).reduce(Predicate::and).orElse(t -> true))
			.map(function)
			.distinct()
			.collect(Collectors.toList());
	}

	/**
	 * @param originList 原数据
	 * @param mappers 映射规则集合
	 * @return List<T>
	 */
	@SafeVarargs
	public static <R> List<R> filterBlankDistinctToMapList(List<String> originList, Function<String, R> function,
			Predicate<String>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeFilter(originList, StrUtil::isBlank);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.filter(Stream.of(mappers).reduce(Predicate::and).orElse(t -> true))
			.map(function)
			.distinct()
			.collect(Collectors.toList());
	}

	/**
	 * 将List映射为List，比如List<Person> personList转为List<String> nameList
	 * @param originList 原数据
	 * @param mappers 映射规则集合
	 * @param <T> 原数据的元素类型
	 * @return List<T>
	 */
	@SafeVarargs
	public static <T> List<String> mapToFiltersBlankDistinctList(List<T> originList, Function<T, String> function,
			Predicate<String>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.map(function)
			.filter(Stream.of(mappers).reduce(Predicate::and).orElse(t -> true))
			.filter(StrUtil::isNotBlank)
			.distinct()
			.collect(Collectors.toList());
	}

	/**
	 * 将List映射为List，比如List<Person> personList转为List<String> nameList
	 * @param originList 原数据
	 * @param mappers 映射规则集合
	 * @return List<T>
	 */
	@SafeVarargs
	public static List<String> filterBlankToDistinctList(List<String> originList, Predicate<String>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeFilter(originList, StrUtil::isBlank);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.filter(Stream.of(mappers).reduce(Predicate::and).orElse(t -> true))
			.distinct()
			.collect(Collectors.toList());
	}

	/**
	 * list根据对象指定属性去重
	 * @param list：要操作的list集合
	 * @param keyExtractor: 去重属性
	 * @return List<T>
	 */
	public static <T> List<T> distinctBy(List<T> list, Function<? super T, ?> keyExtractor) {
		return list.stream().filter(distinctByKey(keyExtractor)).collect(Collectors.toList());
	}

	/**
	 * 根据指定属性去重
	 */
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	/**
	 * 删除空
	 * @param list 列表
	 * @return {@link List}<{@link T}>
	 */
	public static <T> List<T> removeNull(List<T> list) {
		return removeFilter(list, Objects::isNull);
	}

	/**
	 * 过滤移除
	 * @param originList 源列表
	 * @param removeConditions 删除条件
	 * @return {@link List}<{@link T}>
	 */
	@SafeVarargs
	public static <T> List<T> removeFilter(List<T> originList, Predicate<? super T>... removeConditions) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}

		return originList.stream()
			.filter(item -> Stream.of(removeConditions).noneMatch(removeCondition -> removeCondition.test(item)))
			.collect(Collectors.toList());
	}

	/**
	 * 找到
	 * @param originList 原数据
	 * @param mappers 映射规则集合
	 * @param <T> 原数据的元素类型
	 * @return Optional<T>
	 */
	@SafeVarargs
	public static <T> Optional<T> findFirst(List<T> originList, Predicate<T>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return Optional.empty();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return Optional.empty();
		}
		try {
			Predicate<T> combinedPredicate = Stream.of(mappers)
				.filter(Objects::nonNull)
				.reduce(Predicate::and)
				.orElse(t -> true);
			return originList.stream().filter(Objects::nonNull).filter(combinedPredicate).findFirst();
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}

	@SafeVarargs
	public static <T> T findFirstToFilter(List<T> originList, Predicate<T>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return null;
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return null;
		}
		try {
			return originList.stream()
				.filter(Stream.of(mappers).reduce(Predicate::and).orElse(t -> true))
				.findFirst()
				.orElse(null);
		}
		catch (Exception e) {
			return null;
		}
	}

	@SafeVarargs
	public static <T, U> U findFirstMapToFilter(List<T> originList, Function<T, U> mapper, Predicate<U>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return null;
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return null;
		}
		try {
			return originList.stream()
				.map(mapper)
				.filter(Stream.of(mappers).reduce(Predicate::and).orElse(t -> true))
				.findFirst()
				.orElse(null);
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * 匹配
	 * @param originList 原数据
	 * @param mapper 映射规则
	 * @param <T> 原数据的元素类型
	 * @return boolean
	 */
	public static <T> boolean anyMatch(List<T> originList, Predicate<T> mapper) {
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		return originList.stream().anyMatch(mapper);
	}

	/**
	 * 不匹配
	 * @param originList 原数据
	 * @param mapper 映射规则
	 * @param <T> 原数据的元素类型
	 * @return boolean
	 */
	public static <T> boolean noneMatch(List<T> originList, Predicate<T> mapper) {
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		return originList.stream().noneMatch(mapper);
	}

	/**
	 * map后match
	 * @param originList 原数据
	 * @param mapper 映射规则
	 * @param <T> 原数据的元素类型
	 * @param <R> 新数据的元素类型
	 * @return boolean
	 */
	public static <T, R> boolean mapAfterAnyMatch(List<T> originList, Function<T, R> mapper, Predicate<R> predicate) {
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		return originList.stream().map(mapper).anyMatch(predicate);
	}

	/**
	 * map后match
	 * @param originList 原数据
	 * @param mapper 映射规则
	 * @param <T> 原数据的元素类型
	 * @param <R> 新数据的元素类型
	 * @return boolean
	 */
	public static <T, R> boolean mapAfterDistinctAnyMatch(List<T> originList, Function<T, R> mapper,
			Predicate<R> predicate) {
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return false;
		}
		return originList.stream().map(mapper).distinct().anyMatch(predicate);
	}

	/**
	 * 将List映射为List，比如List<Person> personList转为List<String> nameList
	 * @param originList 原数据
	 * @param mappers 映射规则
	 * @param <T> 原数据的元素类型
	 * @param <R> 新数据的元素类型
	 * @return List<R>
	 */
	@SafeVarargs
	public static <T, R> List<R> mapToList(List<T> originList, Function<T, R>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.flatMap(t -> Arrays.stream(mappers).filter(Objects::nonNull).map(f -> f.apply(t)).filter(Objects::nonNull))
			.collect(Collectors.toList());
	}

	/**
	 * 将List映射为List，比如List<Person> personList转为List<String> nameList
	 * @param originList 原数据
	 * @param mappers 映射规则
	 * @param <T> 原数据的元素类型
	 * @param <R> 新数据的元素类型
	 * @return List<R>
	 */
	@SafeVarargs
	public static <T, R> List<R> mapToDistinctList(List<T> originList, Function<T, R>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.flatMap(t -> Arrays.stream(mappers).filter(Objects::nonNull).map(f -> f.apply(t)).filter(Objects::nonNull))
			.distinct()
			.collect(Collectors.toList());
	}

	public static <T> List<T> toDistinctList(List<T> originList) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream().distinct().collect(Collectors.toList());
	}

	@SafeVarargs
	public static <T, R> List<R> mapFilterToDistinctList(List<T> originList, Function<T, R> mapper,
			Predicate<R>... filters) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.filter(Objects::nonNull)
			.map(mapper)
			.distinct()
			.filter(Stream.of(filters).reduce(Predicate::and).orElse(t -> true))
			.collect(Collectors.toList());
	}

	/**
	 * 将List映射为List，比如List<Person> personList转为List<String> nameList
	 * @param originList 原数据
	 * @param mapper 映射规则
	 * @param <T> 原数据的元素类型
	 * @param <R> 新数据的元素类型
	 * @return List<R>
	 */
	@SuppressWarnings({ "all" })
	public static <T, R> List<R> mapToFiltersList(List<T> originList, Function<T, R> mapper, Predicate<R>... filters) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.filter(Objects::nonNull)
			.map(mapper)
			.filter(Stream.of(filters).reduce(Predicate::and).orElse(t -> true))
			.collect(Collectors.toList());
	}

	/**
	 * 将List映射为List，比如List<Person> personList转为List<String> nameList
	 * @param originList 原数据
	 * @param mapper 映射规则
	 * @param <T> 原数据的元素类型
	 * @param <R> 新数据的元素类型
	 * @return List<R>
	 */
	@SuppressWarnings({ "all" })
	public static <T, R> List<R> mapDistinctToFiltersList(List<T> originList, Function<T, R> mapper,
			Predicate<R>... filters) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.filter(Objects::nonNull)
			.map(mapper)
			.distinct()
			.filter(Stream.of(filters).reduce(Predicate::and).orElse(t -> true))
			.collect(Collectors.toList());
	}

	/**
	 * 将List转为Map
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, V>
	 */
	public static <K, V> Map<K, V> listToBeanMap(List<V> originList, Function<V, K> keyExtractor) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream()
			.filter(Objects::nonNull)
			.filter(element -> keyExtractor.apply(element) != null)
			.collect(Collectors.toMap(keyExtractor, Function.identity(), (v1, v2) -> v1));
	}

	/**
	 * 将List转为Map
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, V>
	 */
	public static <K, V, S> Map<K, S> listToMap(List<V> originList, Function<V, K> keyExtractor,
			Function<V, S> valueExtractor) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream()
			.filter(item -> keyExtractor.apply(item) != null && valueExtractor.apply(item) != null)
			.collect(Collectors.toMap(keyExtractor, valueExtractor, (k1, k2) -> k1));
	}

	/**
	 * 将List转为Map
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, V>
	 */
	public static <K, V, S> Map<K, S> listToMap(List<V> originList, Function<V, K> keyExtractor,
			Function<V, S> valueExtractor, BinaryOperator<S> operator) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream()
			.filter(item -> keyExtractor.apply(item) != null && valueExtractor.apply(item) != null)
			.collect(Collectors.toMap(keyExtractor, valueExtractor, operator));
	}

	/**
	 * 将List转为Map
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, V>
	 */
	public static <K, V> Map<K, V> listToBeanMap(List<V> originList, Function<V, K> keyExtractor,
			BinaryOperator<V> operator) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream()
			.filter(item -> keyExtractor.apply(item) != null)
			.collect(Collectors.toMap(keyExtractor, v -> v, operator));
	}

	/**
	 * 将List转为Map
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, V>
	 */
	public static <K, V, S> Map<K, S> listFilterToMap(List<V> originList, Predicate<V> filter,
			Function<V, K> keyExtractor, Function<V, S> valueExtractor, BinaryOperator<S> operator) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream()
			.filter(filter)
			.filter(item -> keyExtractor.apply(item) != null && valueExtractor.apply(item) != null)
			.collect(Collectors.toMap(keyExtractor, valueExtractor, operator));
	}

	/**
	 * 将List转为Map
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, V>
	 */
	public static <K, V, S> Map<K, S> listFiltersToMap(List<V> originList, Function<V, K> keyExtractor,
			Function<V, S> valueExtractor, BinaryOperator<S> operator, Predicate<V>... mappers) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream()
			.filter(Arrays.stream(mappers).reduce(Predicate::and).orElse(t -> true))
			.filter(item -> keyExtractor.apply(item) != null && valueExtractor.apply(item) != null)
			.collect(Collectors.toMap(keyExtractor, valueExtractor, operator));
	}

	/**
	 * 将List分组
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, List < V>>
	 */
	public static <K, V> Map<K, List<V>> groupByToMap(List<V> originList, Function<V, K> keyExtractor) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream().collect(Collectors.groupingBy(keyExtractor));
	}

	/**
	 * 将List分组
	 * @param originList 原数据
	 * @param keyExtractor Key的抽取规则
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map<K, List < V>>
	 */
	public static <K, V, U> Map<K, List<U>> groupByToMap(List<V> originList, Function<V, K> keyExtractor,
			Function<V, U> valueExtractor) {
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new HashMap<>();
		}
		return originList.stream()
			.collect(Collectors.groupingBy(keyExtractor, Collectors.mapping(valueExtractor, Collectors.toList())));
	}

	/**
	 * 按照属性排序
	 * @param originList 原数据
	 * @param <T> 原数据的元素类型
	 * @return List<T>
	 */
	public static <T, U extends Comparable<? super U>> List<T> sortAscLastNull(List<T> originList,
			Function<? super T, ? extends U> function) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.collect(Collectors.collectingAndThen(
					Collectors.toCollection(
							() -> new TreeSet<>(Comparator.comparing(function, Comparator.nullsLast(U::compareTo)))),
					ArrayList::new));
	}

	/**
	 * 按照属性排序
	 * @param originList 原数据
	 * @param <T> 原数据的元素类型
	 * @return List<T>
	 */
	public static <T, U extends Comparable<? super U>> List<T> sortAscFirstNull(List<T> originList,
			Function<? super T, ? extends U> function) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.sorted(Comparator.comparing(function, Comparator.nullsFirst(U::compareTo)))
			.collect(Collectors.toList());
	}

	/**
	 * 按照属性排序倒序
	 * @param originList 源列表
	 * @param mapper 方法
	 * @return {@link String}
	 */
	public static <T, U extends Comparable<? super U>> List<T> sortDescLastNull(List<T> originList,
			Function<? super T, ? extends U> mapper) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.sorted(Comparator.comparing(mapper, Comparator.nullsLast(Comparator.reverseOrder())))
			.collect(Collectors.toList());
	}

	/**
	 * 按照属性排序倒序
	 * @param originList 源列表
	 * @param mapper 方法
	 * @return {@link String}
	 */
	public static <T, U extends Comparable<? super U>> List<T> sortDescFirstNull(List<T> originList,
			Function<? super T, ? extends U> mapper) {
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		originList = removeNull(originList);
		if (CollUtil.isEmpty(originList)) {
			return new ArrayList<>();
		}
		return originList.stream()
			.collect(Collectors.collectingAndThen(Collectors.toCollection(
					() -> new TreeSet<>(Comparator.comparing(mapper, Comparator.nullsFirst(U::compareTo)).reversed())),
					ArrayList::new));
	}

	/**
	 * 计算两个对象列表的交集、并集和差集
	 * @param oldList 旧对象列表
	 * @param newList 新对象列表
	 * @param mapper 判断两个对象是否相等的函数式接口
	 * @param <T> 对象类型
	 * @return 包含三个列表的数组，分别为差集中需要添加的对象列表、差集中需要删除的对象列表和交集的对象列表
	 */
	public static <T> List<T>[] getChangeCudAttr(List<T> oldList, List<T> newList, BiFunction<T, T, Boolean> mapper) {
		// 计算交集
		List<T> existsList = oldList.stream()
			.filter(s -> newList.stream().anyMatch(t -> mapper.apply(t, s)))
			.collect(Collectors.toList());

		// 计算差集（新增的对象，即在新列表中，但是不在旧列表中的对象）
		List<T> stayAddIds = newList.stream()
			.filter(s -> existsList.stream().noneMatch(t -> mapper.apply(t, s)))
			.collect(Collectors.toList());

		// 计算差集（删除的对象，即在旧列表中，但是不在新列表中的对象）
		List<T> stayDelIds = oldList.stream()
			.filter(s -> existsList.stream().noneMatch(t -> mapper.apply(t, s)))
			.collect(Collectors.toList());
		return new List[] { stayAddIds, stayDelIds, existsList };
	}

}
