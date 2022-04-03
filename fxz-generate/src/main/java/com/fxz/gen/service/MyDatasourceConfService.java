package com.fxz.gen.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.gen.entity.DatasourceConf;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-31 11:42
 */
public interface MyDatasourceConfService {

	/**
	 * 分页查询数据源管理信息
	 * @param page 分页参数
	 * @param emptyWrapper 查询条件
	 * @return
	 */
	Page<DatasourceConf> pageDataSourceConf(Page<DatasourceConf> page, QueryWrapper<DatasourceConf> emptyWrapper);

	/**
	 * 动态添加数据源
	 * @param datasourceConf 数据源信息
	 */
	Boolean addDs(DatasourceConf datasourceConf);

	/**
	 * 根据id查询数据源信息
	 * @param id 数据源id
	 * @return 数据源信息
	 */
	DatasourceConf findBtId(Long id);

	/**
	 * 修改数据源信息
	 * @param datasourceConf 数据源信息
	 * @return ture Or false
	 */
	Boolean updateDsConf(DatasourceConf datasourceConf);

	Boolean delete(Long id);

	/**
	 * 查询所有数据源信息
	 */
	List<DatasourceConf> listDs();

}
