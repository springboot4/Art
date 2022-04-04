package com.fxz.common.mp.result;

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

	private static final long serialVersionUID = 7095488598505654202L;

	private String code;

	private List<T> records;

	private long total;

	private long current;

	private long size;

	private String msg;

	public static <T> PageResult<T> success(IPage<T> page) {
		PageResult<T> result = new PageResult<>();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setRecords(page.getRecords());
		result.setTotal(page.getTotal());
		result.setCurrent(page.getCurrent());
		result.setSize(page.getSize());
		result.setMsg(ResultCode.SUCCESS.getMsg());
		return result;
	}

}
