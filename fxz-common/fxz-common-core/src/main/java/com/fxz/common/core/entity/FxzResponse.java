package com.fxz.common.core.entity;

import java.util.HashMap;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:30
 */
public class FxzResponse extends HashMap<String, Object> {

	public FxzResponse message(String message) {
		this.put("message", message);
		return this;
	}

	public FxzResponse data(Object data) {
		this.put("data", data);
		return this;
	}

	@Override
	public FxzResponse put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public String getMessage() {
		return String.valueOf(get("message"));
	}

	public Object getData() {
		return get("data");
	}

}
