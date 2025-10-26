package com.art.core.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/10/25 20:25
 */
public class CollectionUtil extends cn.hutool.core.collection.CollectionUtil {

	public static Map<String, Object> safeMap(Map<String, Object> source) {
		return source == null ? new HashMap<>() : new HashMap<>(source);
	}

}
