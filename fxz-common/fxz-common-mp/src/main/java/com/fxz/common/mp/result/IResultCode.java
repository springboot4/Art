package com.fxz.common.mp.result;

/**
 * @author fxz
 **/
public interface IResultCode {

	/**
	 * 获取状态码
	 * @return 状态码
	 */
	String getCode();

	/**
	 * 获取消息
	 * @return 返回的信息
	 */
	String getMsg();

}
