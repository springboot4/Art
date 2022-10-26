package com.fxz.common.dataPermission.local;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fxz.common.dataPermission.annotation.DataPermission;

import java.util.LinkedList;

/**
 * 数据权限注解上下文
 *
 * @author fxz
 */
public class DataPermissionContextHolder {

	/**
	 * ttl解决父子线程传值问题 由于存在方法的嵌套调用 所以使用List
	 */
	private static final ThreadLocal<LinkedList<DataPermission>> DATA_PERMISSIONS = TransmittableThreadLocal
			.withInitial(LinkedList::new);

	/**
	 * 获得当前的数据权限注解
	 * @return 数据权限 注解
	 */
	public static DataPermission get() {
		return DATA_PERMISSIONS.get().peekLast();
	}

	/**
	 * 入栈数据权限注解
	 * @param dataPermission 数据权限注解
	 */
	public static void add(DataPermission dataPermission) {
		DATA_PERMISSIONS.get().addLast(dataPermission);
	}

	/**
	 * 出栈数据权限注解
	 */
	public static void remove() {
		DATA_PERMISSIONS.get().removeLast();
		// 无元素时 清空 ThreadLocal
		if (DATA_PERMISSIONS.get().isEmpty()) {
			DATA_PERMISSIONS.remove();
		}
	}

}
