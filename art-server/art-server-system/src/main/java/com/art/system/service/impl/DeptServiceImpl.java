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

package com.art.system.service.impl;

import cn.hutool.core.lang.Opt;
import com.art.common.core.exception.FxzException;
import com.art.common.dataPermission.annotation.DataPermission;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.system.api.dept.dto.DeptDTO;
import com.art.system.core.bo.DeptBO;
import com.art.system.core.convert.DeptConvert;
import com.art.system.dao.redis.dept.DeptRedisKeyConstants;
import com.art.system.manager.DeptManager;
import com.art.system.service.DeptService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 18:39
 */
@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

	private final DeptManager deptManager;

	/**
	 * 获取部门树
	 */
	@Override
	public DeptDTO getDeptTree() {
		return DeptConvert.INSTANCE.convert(deptManager.getDeptTree());
	}

	/**
	 * 根据id删除部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteById(Long id) throws FxzException {
		// 判断是否存在下级部门
		Boolean exists = deptManager.existsSubordinate(id);
		if (exists) {
			throw new FxzException("存在子部门,无法删除！");
		}

		// 删除部门
		return deptManager.deleteById(id) > 0;
	}

	/**
	 * 保存部门信息
	 */
	@Override
	public Boolean addDept(DeptDTO deptDTO) {
		return deptManager.addDept(deptDTO) > 0;
	}

	@Override
	public DeptDTO getDeptById(Long id) {
		return DeptConvert.INSTANCE.convert(deptManager.getDeptById(id));
	}

	@Override
	public Boolean updateById(DeptDTO deptDTO) {
		return deptManager.updateById(deptDTO) > 0;
	}

	/**
	 * 获取当前用户部门(包含父级)
	 * @return 用户所处部门
	 */
	@Override
	public String getDeptNameByUserId() {
		ArtAuthUser user = Opt.of(SecurityUtil.getUser()).get();
		return deptManager.getDeptNameByUserId(user.getDeptId());
	}

	/**
	 * 根据Pid查询下级部门 避免数据权限查询时循环调用
	 */
	@DataPermission(enable = false)
	@Cacheable(value = DeptRedisKeyConstants.CACHE_NAMES, key = "#pId", unless = "#result==null")
	@Override
	public List<DeptDTO> getDeptsByParentId(Long pId) {
		DeptBO deptBO = deptManager.getDeptsByParentId(pId);

		return DeptConvert.INSTANCE.convert(treeToList(deptBO));
	}

	private List<DeptBO> treeToList(DeptBO deptBO) {
		if (Objects.isNull(deptBO)) {
			return new ArrayList<>(0);
		}

		LinkedList<DeptBO> result = new LinkedList<>();
		result.add(deptBO);

		List<DeptBO> children = deptBO.getChildren();
		if (CollectionUtils.isEmpty(children)) {
			return result;
		}

		children.forEach(item -> {
			List<DeptBO> boList = treeToList(item);
			if (CollectionUtils.isNotEmpty(boList)) {
				result.addAll(boList);
			}
		});

		return result;
	}

}
