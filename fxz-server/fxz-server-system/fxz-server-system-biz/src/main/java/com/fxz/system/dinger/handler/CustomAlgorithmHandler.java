package com.fxz.system.dinger.handler;

import cn.hutool.core.convert.Convert;
import com.github.jaemon.dinger.core.DingerConfig;
import com.github.jaemon.dinger.multi.algorithm.AlgorithmHandler;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * 自定义多机器人选择算法
 */
public class CustomAlgorithmHandler implements AlgorithmHandler {

	static LongAdder longAdder = new LongAdder();

	@Override
	public DingerConfig handler(List<DingerConfig> dingerConfigs, DingerConfig defaultDingerConfig) {
		longAdder.add(1);
		// 多机器人使用逻辑代码...
		return dingerConfigs.get(Convert.toInt(longAdder.longValue() % 4));
	}

}