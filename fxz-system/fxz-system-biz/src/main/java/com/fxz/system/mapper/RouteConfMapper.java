package com.fxz.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxz.system.entity.RouteConf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 路由配置表
 *
 * @author fxz
 * @date 2022-08-20
 */
@Mapper
public interface RouteConfMapper extends BaseMapper<RouteConf> {

	List<RouteConf> findAll();

}