package com.fxz.serversystem.service;

import com.fxz.common.core.entity.FxzServerConstant;
import com.fxz.common.core.entity.system.TradeLog;
import com.fxz.serversystem.service.fallback.RemoteTradeLogServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author fxz
 */
@FeignClient(value = FxzServerConstant.FXZ_SERVER_TEST, contextId = "tradeLogServiceClient",
		fallbackFactory = RemoteTradeLogServiceFallback.class)
public interface IRemoteTradeLogService {

	@PostMapping("/package/send")
	void packageAndSend(@RequestBody TradeLog tradeLog);

}