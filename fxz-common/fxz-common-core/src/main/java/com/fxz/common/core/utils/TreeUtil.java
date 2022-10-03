package com.fxz.common.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.fxz.common.core.entity.router.VueRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fxz
 */
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
				.collect(Collectors.toMap(VueRouter::getId, v -> v, (k1, k2) -> k2));

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