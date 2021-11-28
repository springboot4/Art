package com.fxz.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.entity.system.Menu;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 15:26
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过用户名查找用户权限集合
     */
    List<Menu> findUserPermissions(String username);
}
