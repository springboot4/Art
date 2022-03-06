package com.fxz.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.entity.TradeLog;

/**
 * @author fxz
 */
public interface ITradeLogService extends IService<TradeLog> {

	void orderAndPay(TradeLog tradeLog);

}