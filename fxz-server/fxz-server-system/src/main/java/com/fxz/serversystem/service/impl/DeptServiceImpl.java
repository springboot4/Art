package com.fxz.serversystem.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.entity.system.Dept;
import com.fxz.common.core.exception.FxzException;
import com.fxz.serversystem.mapper.DeptMapper;
import com.fxz.serversystem.service.IDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:39
 */
@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

	/**
	 * 获取部门树
	 */
	@Override
	public Dept getDeptTree() {
		return this.baseMapper.getDeptTree();
	}

	/**
	 * 根据id删除部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean delete(Long id) throws FxzException {
		// 判断是否存在下级
		Integer count = this.getBaseMapper().selectCount(Wrappers.<Dept>lambdaQuery().eq(Dept::getParentId, id));
		if (count > 0) {
			throw new FxzException("存在子部门,无法直接删除");
		}
		// 删除数据
		return this.getBaseMapper().deleteById(id) > 0;
	}

	/**
	 * 保存部门信息
	 */
	@Override
	public Boolean addDept(Dept dept) {
		dept.setCreateTime(new Date());
		dept.setModifyTime(new Date());
		return this.getBaseMapper().insert(dept) > 0;
	}

}
