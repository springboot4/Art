package com.fxz.system.vo;

import com.fxz.system.entity.Tenant;
import lombok.Data;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 18:49
 */
@Data
public class TenantVO extends Tenant {

	private String username;

	private String password;

}
