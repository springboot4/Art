package com.fxz.common.core.utils;

import com.fxz.common.core.entity.router.VueRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fxz
 */
public class TreeUtil {

	/**
	 * 顶级节点 ID
	 */
	private final static String TOP_NODE_ID = "0";

	/**
	 * 构造前端路由
	 * @param routes routes
	 * @param <T> T
	 * @return ArrayList<VueRouter < T>>
	 */
	public static <T> List<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
		if (routes == null) {
			return null;
		}
		List<VueRouter<T>> topRoutes = new ArrayList<>();
		routes.forEach(route -> {
			String parentId = route.getParentId();
			if (TOP_NODE_ID.equals(parentId)) {
				topRoutes.add(route);
				return;
			}
			for (VueRouter<T> parent : routes) {
				String id = parent.getId();
				if (id != null && id.equals(parentId)) {
					if (parent.getChildren() == null) {
						parent.initChildren();
					}

					parent.getChildren().add(route);
					parent.setAlwaysShow(true);
					parent.setHasChildren(true);
					route.setHasParent(true);
					parent.setHasParent(true);
					return;
				}
			}
		});

		return topRoutes;
	}

}