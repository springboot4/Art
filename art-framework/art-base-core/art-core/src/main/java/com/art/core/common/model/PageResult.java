/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.core.common.model;

import com.art.core.common.constant.ResultCode;
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
		result.setMsg(ResultCode.SUCCESS.getMsg());
		result.setRecords(page.getRecords());
		result.setTotal(page.getTotal());
		result.setCurrent(page.getCurrent());
		result.setSize(page.getSize());
		return result;
	}

}
