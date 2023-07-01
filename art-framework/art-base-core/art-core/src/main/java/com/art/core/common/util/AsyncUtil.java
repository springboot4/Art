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

package com.art.core.common.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 简化CompletableFuture使用
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2023/2/5 17:06
 */
@UtilityClass
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AsyncUtil {

	public static CompletableFuture<Void> run(Runnable... runs) {
		return CompletableFuture.allOf(warpCompletableFuture(true, runs));
	}

	public static void parallel(Runnable... runs) {
		run(runs).join();
	}

	public static void parallel(Boolean ignoreException, Runnable... runs) {
		CompletableFuture.allOf(warpCompletableFuture(ignoreException, runs)).join();
	}

	public static Object[] parallel(Supplier... suppliers) {
		return parallel(true, suppliers);
	}

	public static Object[] parallel(Boolean ignoreException, Supplier... suppliers) {
		CompletableFuture<Object>[] completableFutures = warpCompletableFuture(ignoreException, suppliers);
		CompletableFuture.allOf(completableFutures).join();
		Object[] objects = new Object[completableFutures.length];

		for (int i = 0; i < completableFutures.length; ++i) {
			try {
				objects[i] = completableFutures[i].get();
			}
			catch (Exception e) {
				if (!ignoreException) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}

		return objects;
	}

	private static CompletableFuture[] warpCompletableFuture(Boolean ignoreException, Supplier... suppliers) {
		CompletableFuture<Object>[] completableFutures = new CompletableFuture[suppliers.length];

		for (int i = 0; i < suppliers.length; ++i) {
			Supplier<Object> supplier = suppliers[i];
			completableFutures[i] = CompletableFuture.supplyAsync(() -> {
				Object res;
				try {
					res = supplier.get();
					return res;
				}
				catch (Exception e) {
					if (!ignoreException) {
						throw new RuntimeException(e.getMessage());
					}

					res = null;
				}
				return res;
			});
		}

		return completableFutures;
	}

	private static CompletableFuture<Void>[] warpCompletableFuture(Boolean ignoreException, Runnable... runs) {
		CompletableFuture<Void>[] completableFutures = new CompletableFuture[runs.length];

		for (int i = 0; i < runs.length; ++i) {
			Runnable runnable = runs[i];
			completableFutures[i] = CompletableFuture.runAsync(() -> {
				try {
					runnable.run();
				}
				catch (Exception e) {
					if (!ignoreException) {
						throw new RuntimeException(e.getMessage());
					}
				}
			});
		}

		return completableFutures;
	}

}
