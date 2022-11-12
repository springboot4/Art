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

package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.exception.FxzException;
import com.fxz.common.dataPermission.annotation.DataPermission;
import com.fxz.common.redis.constant.CacheConstants;
import com.fxz.system.entity.Dept;
import com.fxz.system.mapper.DeptMapper;
import com.fxz.system.service.IDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
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
		Long count = this.getBaseMapper().selectCount(Wrappers.<Dept>lambdaQuery().eq(Dept::getParentId, id));
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
		return this.getBaseMapper().insert(dept) > 0;
	}

	/**
	 * 根据Pid查询下级部门 避免数据权限查询时循环调用
	 */
	@DataPermission(enable = false)
	@Cacheable(value = CacheConstants.GLOBALLY + "dept:", key = "#pId", unless = "#result==null")
	@Override
	public List<Dept> getDeptsByParentId(Long pId) {
		Dept dept = this.baseMapper.getDeptsByParentId(pId);

		return treeToList(dept);
	}

	private List<Dept> treeToList(Dept dept) {
		LinkedList<Dept> list = new LinkedList<>();
		list.add(dept);

		List<Dept> children = dept.getChildren();
		if (CollectionUtils.isEmpty(children)) {
			return list;
		}

		children.forEach(item -> {
			List<Dept> depts = treeToList(item);
			if (CollectionUtils.isNotEmpty(depts)) {
				list.addAll(depts);
			}
		});

		return list;
	}

}
