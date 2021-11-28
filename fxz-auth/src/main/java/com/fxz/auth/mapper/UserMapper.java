package com.fxz.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.entity.system.SystemUser;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 15:26
 */
public interface UserMapper  extends BaseMapper<SystemUser> {

    /**
     * 通过用户名查找用户信息
     */
    SystemUser findByName(String username);
}
