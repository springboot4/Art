package com.fxz.serversystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.entity.system.Menu;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-29 18:28
 */
public interface MenuMapper  extends BaseMapper<Menu> {

    /**
     * 通过用户名查询权限信息
     *
     * @param username 用户名称
     * @return 权限信息
     */
    List<Menu> findUserPermissions(String username);

    /**
     * 通过用户名查询菜单信息
     *
     * @param username 用户名
     * @return 菜单信息
     */
    List<Menu> findUserMenus(String username);

}
