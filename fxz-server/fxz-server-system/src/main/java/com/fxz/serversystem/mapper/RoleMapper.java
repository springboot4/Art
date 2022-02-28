package com.fxz.serversystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxz.common.core.entity.system.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 17:52
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

	/**
	 * 根据id获取角色信息
	 */
	Role getRoleById(Long id);

}
