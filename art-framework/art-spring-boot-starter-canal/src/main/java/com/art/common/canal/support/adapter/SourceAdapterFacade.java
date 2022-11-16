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

package com.art.common.canal.support.adapter;

/**
 * @author fxz
 */
public enum SourceAdapterFacade {

	/**
	 * 单例
	 */
	X;

	private static final SourceAdapter<String, String> I_S_A = RawStringSourceAdapter.of();

	@SuppressWarnings("unchecked")
	public <T> T adapt(Class<T> klass, String source) {
		if (klass.isAssignableFrom(String.class)) {
			return (T) I_S_A.adapt(source);
		}
		return FastJsonSourceAdapter.of(klass).adapt(source);
	}

}
