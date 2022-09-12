package com.fxz.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxz.system.entity.App;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
@Mapper
public interface AppMapper extends BaseMapper<App> {

}