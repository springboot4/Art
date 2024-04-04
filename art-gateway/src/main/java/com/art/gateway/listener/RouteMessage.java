package com.art.gateway.listener;

import com.art.mq.sdk.support.broadcast.RedisBroadcastMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteMessage extends RedisBroadcastMessage implements Serializable {

	private static final long serialVersionUID = -1L;

	List<RouteConfDTO> routeConfDOList;

	@Override
	public String getTopic() {
		return "route.redis.gateway.topic";
	}

}