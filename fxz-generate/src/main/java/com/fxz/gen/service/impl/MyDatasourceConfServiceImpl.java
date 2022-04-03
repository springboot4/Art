package com.fxz.gen.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.gen.entity.DatasourceConf;
import com.fxz.gen.mapper.DatasourceConfMapper;
import com.fxz.gen.service.MyDatasourceConfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据源表
 *
 * @author fxz
 * @date 2022-03-31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyDatasourceConfServiceImpl implements MyDatasourceConfService {

	private final StringEncryptor stringEncryptor;

	private final DataSource dataSource;

	private final DefaultDataSourceCreator dataSourceCreator;

	private final DatasourceConfMapper datasourceConfMapper;

	/**
	 * 分页查询数据源管理信息
	 * @param page 分页参数
	 * @param emptyWrapper 查询条件
	 * @return
	 */
	@Override
	public Page<DatasourceConf> pageDataSourceConf(Page<DatasourceConf> page,
			QueryWrapper<DatasourceConf> emptyWrapper) {
		return datasourceConfMapper.selectPage(page, emptyWrapper);
	}

	/**
	 * 动态添加数据源
	 * @param datasourceConf 数据源信息
	 */
	@Override
	public Boolean addDs(DatasourceConf datasourceConf) {
		// 校验配置合法性
		Assert.isTrue(checkDataSource(datasourceConf), "数据源信息错误，连接失败!");

		// 添加动态数据源
		addDynamicDataSource(datasourceConf);

		// 更新数据库信息，加密数据库密码
		datasourceConf.setPassword(stringEncryptor.encrypt(datasourceConf.getPassword()));

		return datasourceConfMapper.insert(datasourceConf) > 0;
	}

	/**
	 * 修改数据源信息
	 * @param datasourceConf 数据源信息
	 * @return ture Or false
	 */
	@Override
	public Boolean updateDsConf(DatasourceConf datasourceConf) {
		// 校验配置合法性
		Assert.isTrue(checkDataSource(datasourceConf), "数据源信息错误，连接失败!");

		// 移除数据源
		DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
		ds.removeDataSource(datasourceConf.getName());

		// 添加数据源
		addDynamicDataSource(datasourceConf);

		// 密码加密并且更新数据源信息
		datasourceConf.setPassword(stringEncryptor.encrypt(datasourceConf.getPassword()));
		return datasourceConfMapper.updateById(datasourceConf) > 0;
	}

	/**
	 * 删除数据源信息
	 * @param id 数据源主键
	 */
	@Override
	public Boolean delete(Long id) {
		// 查询数据源信息
		DatasourceConf datasourceConf = datasourceConfMapper.selectById(id);

		// 移除数据源
		DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
		ds.removeDataSource(datasourceConf.getName());

		// 删除数据源信息
		return datasourceConfMapper.deleteById(id) > 0;
	}

	/**
	 * 查询所有数据源信息
	 */
	@Override
	public List<DatasourceConf> listDs() {
		return datasourceConfMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据id查询数据源信息
	 * @param id 数据源id
	 * @return 数据源信息
	 */
	@Override
	public DatasourceConf findBtId(Long id) {
		return datasourceConfMapper.selectById(id);
	}

	/**
	 * 添加动态数据源 通用数据源会根据maven中配置的连接池根据顺序依次选择。 默认的顺序为druid>hikaricp>beecp>dbcp>spring basic
	 * @param conf 数据源信息
	 */
	public void addDynamicDataSource(DatasourceConf conf) {
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPoolName(conf.getName());
		dataSourceProperty.setUrl(conf.getUrl());
		dataSourceProperty.setUsername(conf.getUsername());
		dataSourceProperty.setPassword(conf.getPassword());
		dataSourceProperty.setLazy(true);

		DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
		DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);

		ds.addDataSource(conf.getName(), dataSource);
	}

	/**
	 * 校验数据源配置是否有效
	 * @param conf 数据源信息
	 * @return 有效/无效
	 */
	public Boolean checkDataSource(DatasourceConf conf) {
		try {
			DriverManager.getConnection(conf.getUrl(), conf.getUsername(), conf.getPassword());
		}
		catch (SQLException e) {
			log.error("数据源配置 {} , 获取链接失败", conf.getName(), e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}