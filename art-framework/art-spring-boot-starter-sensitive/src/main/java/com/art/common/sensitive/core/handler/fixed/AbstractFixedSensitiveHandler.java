package com.art.common.sensitive.core.handler.fixed;

import com.art.common.sensitive.core.handler.base.SensitiveHandler;

import java.lang.annotation.Annotation;

/**
 * @author fxz
 */
public abstract class AbstractFixedSensitiveHandler<T extends Annotation> implements SensitiveHandler<T> {

	@Override
	public String desensitize(String origin, T annotation) {
		int front = getFront(annotation);
		int end = getEnd(annotation);
		String replacer = getReplacer(annotation);
		int length = origin.length();

		if (front >= length || end >= length || (front + end) >= length) {
			return buildReplacerByLength(replacer, length);
		}

		int interval = length - front - end;
		return origin.substring(0, front) + buildReplacerByLength(replacer, interval)
				+ origin.substring(front + interval);
	}

	/**
	 * 根据长度循环构建替换符
	 * @param replacer 替换符
	 * @param length 长度
	 * @return 构建后的替换符
	 */
	private String buildReplacerByLength(String replacer, int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			builder.append(replacer);
		}
		return builder.toString();
	}

	/**
	 * 前缀保留长度
	 * @param annotation 注解信息
	 * @return 前缀保留长度
	 */
	abstract Integer getFront(T annotation);

	/**
	 * 后缀保留长度
	 * @param annotation 注解信息
	 * @return 后缀保留长度
	 */
	abstract Integer getEnd(T annotation);

	/**
	 * 替换符
	 * @param annotation 注解信息
	 * @return 替换符
	 */
	abstract String getReplacer(T annotation);

}
