package com.fxz.common.Idempotent.constant;

/**
 * 幂等常量
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/9 19:37
 */
public interface IdempotentConstant {

	String KEY_PREFIX = "Key";

	String DEL_KEY_PREFIX = "DelKey";

	String REDIS_FORMAT = "idempotent:%s";

}
