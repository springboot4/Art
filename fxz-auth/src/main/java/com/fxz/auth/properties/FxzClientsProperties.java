package com.fxz.auth.properties;

import lombok.Data;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 20:59
 */
@Data
public class FxzClientsProperties {

    private String client;

    private String secret;

    private String grantType = "password,authorization_code,refresh_token";

    private String scope = "all";

}
