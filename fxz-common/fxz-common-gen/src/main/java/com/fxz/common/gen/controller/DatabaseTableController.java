package com.fxz.common.gen.controller;

import com.fxz.common.core.entity.FxzResponse;
import com.fxz.common.core.entity.PageParam;
import com.fxz.common.core.utils.FxzUtil;
import com.fxz.common.gen.entity.DatabaseTable;
import com.fxz.common.gen.service.impl.DatabaseTableServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据库表信息
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-03-04 9:21
 */
@RestController
@RequestMapping("/gen/table")
@RequiredArgsConstructor
public class DatabaseTableController {

	private final DatabaseTableServiceImpl databaseTableService;

	/**
	 * 分页查询基础表信息
	 * @param pageParam 分页参数
	 * @param param 查询参数
	 * @return 分页信息
	 */
	@GetMapping("/page")
	public FxzResponse page(PageParam pageParam, DatabaseTable param) {
		return new FxzResponse().data(FxzUtil.getDataTable(databaseTableService.page(pageParam, param)));
	}

}
