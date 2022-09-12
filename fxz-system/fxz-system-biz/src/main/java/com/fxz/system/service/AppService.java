package com.fxz.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.entity.App;
import com.fxz.system.param.AppParam;

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