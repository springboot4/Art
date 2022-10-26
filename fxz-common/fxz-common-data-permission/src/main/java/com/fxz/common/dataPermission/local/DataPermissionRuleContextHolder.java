package com.fxz.common.dataPermission.local;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fxz.common.dataPermission.rule.DataPermissionRule;

import java.util.List;

/**
 * SQL 解析上下文 方便透传 {@link DataPermissionRule} 规则
 *
 * @author fxz
 */

public class DataPermissionRuleContextHolder {

	/**
	 * MappedStatement 对应的规则
	 */
	private static final ThreadLocal<List<DataPermissionRule>> RULES = new TransmittableThreadLocal<>();

	/**
	 * SQL 是否进行重写
	 */
	private static final ThreadLocal<Boolean> REWRITE = new TransmittableThreadLocal<>();

	public static void init(List<DataPermissionRule> rules) {
		RULES.set(rules);
		REWRITE.set(false);
	}

	public static void clear() {
		RULES.remove();
		REWRITE.remove();
	}

	public static boolean getRewrite() {
		return REWRITE.get();
	}

	public static void setRewrite(boolean rewrite) {
		REWRITE.set(rewrite);
	}

	public static List<DataPermissionRule> getRules() {
		return RULES.get();
	}

}