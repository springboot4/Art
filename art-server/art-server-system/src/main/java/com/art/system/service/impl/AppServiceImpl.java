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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.art.system.entity.App;
import com.art.system.mapper.AppMapper;
import com.art.system.param.AppParam;
import com.art.system.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

	private final AppMapper appMapper;

	/**
	 * 添加
	 */
	@Override
	public Boolean addSysApp(App app) {
		appMapper.insert(app);
		return Boolean.TRUE;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateSysApp(App app) {
		appMapper.updateById(app);
		return Boolean.TRUE;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<App> pageSysApp(Page<App> pageParam, AppParam appParam) {
		return appMapper.selectPage(pageParam, appParam.lambdaQuery());
	}

	/**
	 * 获取单条
	 */
	@Override
	public App findById(Long id) {
		return appMapper.selectById(id);
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<App> findAll() {
		return appMapper.selectList(Wrappers.<App>lambdaQuery().orderByAsc(App::getSort));
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteSysApp(Long id) {
		appMapper.deleteById(id);
		return Boolean.TRUE;
	}

}