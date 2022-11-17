/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.demos.distributed.lock.redis.constant;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/3 15:14
 */
public interface RedisConstant {

	/**
	 * redis加锁lua脚本 如果锁key不存在或者key对于的field为当前线程 执行hincrby自增加锁或重入 返回1 否则返回0表示抢锁失败
	 */
	String LOCK_LUA_SCRIPT = "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then redis.call('hincrby', KEYS[1], ARGV[1], 1)  redis.call('expire', KEYS[1], ARGV[2]) return 1 else  return 0 end";

	/**
	 * redis解锁lua脚本 如果锁不存在 返回null 否则对field减1
	 */
	String UNLOCK_LUA_SCRIPT = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0  then  return nil  elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then return redis.call('del', KEYS[1])  else  return 0 end";

	/**
	 * redis续期lua脚本
	 */
	String RENEWEXPIRE_LUA_SCRIPT = "if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then  return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";

}
