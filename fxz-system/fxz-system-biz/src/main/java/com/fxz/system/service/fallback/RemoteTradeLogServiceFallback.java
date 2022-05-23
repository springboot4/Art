package com.fxz.system.service.fallback;

import com.fxz.system.service.IRemoteTradeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author fxz
 */
@Slf4j
@Component
public class RemoteTradeLogServiceFallback implements FallbackFactory<IRemoteTradeLogService> {

	@Override
	public IRemoteTradeLogService create(Throwable throwable) {
		return tradeLog -> {
			log.info("调用失败:", throwable);
			throw new RuntimeException();
		};
	}

}