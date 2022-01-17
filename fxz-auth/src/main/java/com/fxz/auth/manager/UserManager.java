package com.fxz.auth.manager;

import com.fxz.common.core.entity.system.Menu;
import com.fxz.common.core.entity.system.SystemUser;
import com.fxz.auth.mapper.MenuMapper;
import com.fxz.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 15:53
 */
@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserMapper userMapper;

    private final MenuMapper menuMapper;

    public SystemUser findByName(String username) {
        return userMapper.findByName(username);
    }

    public String findUserPermissions(String username) {
        List<Menu> userPermissions = menuMapper.findUserPermissions(username);

        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(","));
    }

}
