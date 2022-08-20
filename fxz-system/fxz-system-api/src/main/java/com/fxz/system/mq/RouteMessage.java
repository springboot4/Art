package com.fxz.system.mq;

import com.fxz.common.mq.redis.stream.AbstractStreamMessage;
import com.fxz.system.entity.RouteConf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 网关路由更新消息
 *
 * @author fxz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RouteMessage extends AbstractStreamMessage implements Serializable {

	private static final long serialVersionUID = -1L;

	List<RouteConf> routeConfList;

	@Override
	public String getStreamKey() {
		return "route:redis:gateway:topic";
	}

}