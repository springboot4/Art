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

package com.art.mybatis.sdk.base;

/**
 * 克隆支持 <br/>
 * <p>
 * 在某些特定的场景(如存在各种缓存的情况)下，我们希望
 * <ul>
 * <li>入库加密时，加密后的密文不要回写到原来的对象中 —— 因为在某些业务场景下，入库后，后面的程序逻辑还需要取对应的明文值而不是密文值。</li>
 * <li>出库解密时，解密后的明文不要回写到原来的对象中 —— 因为在某些业务场景下，出库后，后面的一些程序逻辑需要修改查询出来的对象的属性值，
 * 你这里修改后；当别人使用相同的sql查询时，因为某些缓存机制的存在，就可能导致别人查出来的对象就是你现在在操作着的对象，随意这个对象 的值比起数据库数据来说，是"失真"了的
 * </li>
 * </ul>
 */
public interface PojoCloneable<T extends PojoCloneable<T>> {

	/**
	 * 克隆当前对象
	 * @return （以当前对象）克隆出来的对象
	 */
	T clonePojo();

}
