package com.fxz.serversystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.entity.TradeLog;

/**
 * @author fxz
 */
public interface ITestTradeLogService extends IService<TradeLog> {

	void packageAndSend(TradeLog tradeLog);

}