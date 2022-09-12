package com.fxz.system.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fxz.system.entity.App;

import java.util.Objects;

/**
 * 应用查询参数
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/12 13:01
 */
public class AppParam extends App {

	public LambdaQueryWrapper<App> lambdaQuery() {
		return Wrappers.<App>lambdaQuery().like(StrUtil.isNotBlank(this.getName()), App::getName, this.getName())
				.like(StrUtil.isNotBlank(this.getCode()), App::getCode, this.getCode())
				.eq(Objects.nonNull(this.getId()), App::getId, this.getId()).orderByAsc(App::getSort);
	}

}
