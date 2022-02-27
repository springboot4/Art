package com.fxz.serversystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.entity.system.Dept;
import com.fxz.serversystem.mapper.DeptMapper;
import com.fxz.serversystem.service.IDeptService;
import org.springframework.stereotype.Service;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:39
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

	/**
	 * 获取部门树
	 */
	@Override
	public Dept getDeptTree() {
		return this.baseMapper.getDeptTree();
	}

}
