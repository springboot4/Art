package com.fxz.serversystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.entity.system.Role;
import com.fxz.serversystem.mapper.RoleMapper;
import com.fxz.serversystem.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 17:52
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
