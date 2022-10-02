package com.fxz.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxz.system.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户表
 *
 * @author fxz
 * @date 2022-10-01
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

}