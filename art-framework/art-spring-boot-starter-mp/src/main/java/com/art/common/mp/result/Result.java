/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.mp.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结构体
 *
 * @author fxz
 **/
@Data
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 5725721612604634972L;

	private String code;

	private T data;

	private String msg;

	public static <T> Result<T> success() {
		return success(null);
	}

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMsg(ResultCode.SUCCESS.getMsg());
		result.setData(data);
		return result;
	}

	public static <T> Result<T> failed() {
		return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), ResultCode.SYSTEM_EXECUTION_ERROR.getMsg(), null);
	}

	public static <T> Result<T> failed(String msg) {
		return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
	}

	public static <T> Result<T> failed(IResultCode resultCode) {
		return result(resultCode.getCode(), resultCode.getMsg(), null);
	}

	public static <T> Result<T> failed(IResultCode resultCode, String msg) {
		return result(resultCode.getCode(), msg, null);
	}

	public static <T> Result<T> failed(Integer resultCode, String msg) {
		return result(resultCode.toString(), msg, null);
	}

	public static <T> Result<T> judge(boolean status) {
		if (status) {
			return success();
		}
		else {
			return failed();
		}
	}

	private static <T> Result<T> result(IResultCode resultCode, T data) {
		return result(resultCode.getCode(), resultCode.getMsg(), data);
	}

	private static <T> Result<T> result(String code, String msg, T data) {
		Result<T> result = new Result<>();
		result.setCode(code);
		result.setData(data);
		result.setMsg(msg);
		return result;
	}

	public static boolean isSuccess(Result<?> result) {
		return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
	}

}
