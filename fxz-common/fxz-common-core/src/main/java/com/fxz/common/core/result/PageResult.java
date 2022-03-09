package com.fxz.common.core.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结构体
 *
 * @author fxz
 */
@Data
public class PageResult<T> implements Serializable {

	private String code;

	private List<T> records;

	private long total;

	private String msg;

	public static <T> PageResult<T> success(IPage<T> page) {
		PageResult<T> result = new PageResult<>();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setRecords(page.getRecords());
		result.setTotal(page.getTotal());
		result.setMsg(ResultCode.SUCCESS.getMsg());
		return result;
	}

}
