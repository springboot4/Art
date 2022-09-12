package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.system.entity.App;
import com.fxz.system.mapper.AppMapper;
import com.fxz.system.param.AppParam;
import com.fxz.system.service.AppService;
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