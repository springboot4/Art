package com.fxz.system.dinger.handler;

import com.github.jaemon.dinger.core.DingerConfig;
import com.github.jaemon.dinger.multi.algorithm.AlgorithmHandler;

import java.util.List;

/**
 * 自定义多机器人选择算法
 */
public class CustomAlgorithmHandler implements AlgorithmHandler {

	@Override
	public DingerConfig handler(List<DingerConfig> dingerConfigs, DingerConfig defaultDingerConfig) {
		// 多机器人使用逻辑代码...
		return dingerConfigs.get(1);
	}

}