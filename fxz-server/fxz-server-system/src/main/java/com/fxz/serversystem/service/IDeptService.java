package com.fxz.serversystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.common.core.entity.system.Dept;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:38
 */
public interface IDeptService extends IService<Dept> {

	/**
	 * 获取部门树
	 */
	Dept getDeptTree();

}
