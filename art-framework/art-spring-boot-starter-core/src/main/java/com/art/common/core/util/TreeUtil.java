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

package com.art.common.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.art.common.core.model.VueRouter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fxz
 */
@UtilityClass
public class TreeUtil {

	/**
	 * 顶级节点 ID
	 */
	private final static String TOP_NODE_ID = "0";

	/**
	 * 构建树形VueRouter
	 * @param routes routes
	 * @return 树形VueRouter
	 */
	public static <T> List<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
		if (CollectionUtil.isEmpty(routes)) {
			return null;
		}

		List<VueRouter<T>> topRoutes = new ArrayList<>();
		Map<String, VueRouter<T>> routerMap = routes.stream()
				.collect(Collectors.toMap(VueRouter::getId, Function.identity()));

		routes.forEach(route -> {
			String parentId = route.getParentId();
			if (TOP_NODE_ID.equals(parentId)) {
				topRoutes.add(route);
			}
			else {
				VueRouter<T> parent = routerMap.get(parentId);
				if (Objects.isNull(parent)) {
					return;
				}

				if (CollectionUtil.isEmpty(parent.getChildren())) {
					parent.initChildren();
				}

				parent.getChildren().add(route);
				parent.setAlwaysShow(true);
				parent.setHasChildren(true);
				route.setHasParent(true);
				parent.setHasParent(true);
			}
		});

		return topRoutes;
	}

}