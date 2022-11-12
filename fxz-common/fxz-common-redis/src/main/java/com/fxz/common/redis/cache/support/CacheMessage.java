package com.fxz.common.redis.cache.support;

import com.fxz.common.mq.redis.stream.AbstractStreamMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fxz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheMessage extends AbstractStreamMessage implements Serializable {

	private static final long serialVersionUID = 3987211310442078199L;

	private String cacheName;

	private Object key;

	@Override
	public String getStreamKey() {
		return "cache:redis:caffeine:topic";
	}

}
