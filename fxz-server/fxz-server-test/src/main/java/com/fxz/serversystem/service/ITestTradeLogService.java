package com.fxz.serversystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.common.core.entity.system.TradeLog;

/**
 * @author fxz
 */
public interface ITestTradeLogService extends IService<TradeLog> {

	void packageAndSend(TradeLog tradeLog);

}