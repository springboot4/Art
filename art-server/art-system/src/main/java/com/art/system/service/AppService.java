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

package com.art.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.art.system.entity.App;
import com.art.system.param.AppParam;

import java.util.List;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
public interface AppService extends IService<App> {

	/**
	 * 添加
	 */
	Boolean addSysApp(App app);

	/**
	 * 修改
	 */
	Boolean updateSysApp(App app);

	/**
	 * 分页
	 */
	IPage<App> pageSysApp(Page<App> pageParam, AppParam appParam);

	/**
	 * 获取单条
	 */
	App findById(Long id);

	/**
	 * 获取全部
	 */
	List<App> findAll();

	/**
	 * 删除
	 */
	Boolean deleteSysApp(Long id);

}