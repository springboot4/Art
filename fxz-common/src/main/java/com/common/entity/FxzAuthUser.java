package com.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:23
 */
@Data
public class FxzAuthUser implements Serializable {

    private String username;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;
}
