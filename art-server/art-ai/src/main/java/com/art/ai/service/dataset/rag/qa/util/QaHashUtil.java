package com.art.ai.service.dataset.rag.qa.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * QA Hash工具类
 *
 * @author fxz
 * @since 2025/10/12
 */
public class QaHashUtil {

	private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\p{Punct}\\s]+");

	/**
	 * 计算问题的Hash值
	 * @param question 原始问题
	 * @return MD5 Hash
	 */
	public static String calculateQuestionHash(String question) {
		if (question == null || question.isEmpty()) {
			return null;
		}

		// 标准化: 小写 + 去除标点符号 + trim
		String normalized = normalizeQuestion(question);
		return DigestUtils.md5Hex(normalized);
	}

	/**
	 * 标准化问题
	 * @param question 原始问题
	 * @return 标准化后的问题
	 */
	public static String normalizeQuestion(String question) {
		if (question == null || question.isEmpty()) {
			return "";
		}

		// 1. 转小写
		String normalized = question.toLowerCase();

		// 2. Unicode标准化(处理繁简体等)
		normalized = Normalizer.normalize(normalized, Normalizer.Form.NFKC);

		// 3. 去除标点符号和空格
		normalized = PUNCTUATION_PATTERN.matcher(normalized).replaceAll("");

		return normalized.trim();
	}

}
