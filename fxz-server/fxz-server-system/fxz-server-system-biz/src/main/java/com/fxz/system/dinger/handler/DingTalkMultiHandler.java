package com.fxz.system.dinger.handler;

import com.github.jaemon.dinger.core.DingerConfig;
import com.github.jaemon.dinger.multi.DingerConfigHandler;
import com.github.jaemon.dinger.multi.algorithm.AlgorithmHandler;
import com.github.jaemon.dinger.multi.algorithm.RoundRobinHandler;

import java.util.ArrayList;
import java.util.List;

public class DingTalkMultiHandler implements DingerConfigHandler {

	@Override
	public List<DingerConfig> dingerConfigs() {
		List<DingerConfig> dingerConfigs = new ArrayList<>();
		// 注意这里的dingerConfig根据实际情况配置
		dingerConfigs.add(DingerConfig.instance("064899384d58605758ce9a270c87084e9b8a4c297cbc0b8f9e6cafe8bbbe4d53",
				"SEC7817308ccdb72b3aea356c3fe789087906767a651e20b47380fa064db8c3c0e5"));
		dingerConfigs.add(DingerConfig.instance("f808041f94a39f53542d0cab3deade40efbee91db6eb02868bf6fcf79a97817b",
				"SECa1b8b09002469e99222dc7528b68cb71ebb72a05025302ba4f8f23133d16e95c"));
		dingerConfigs.add(DingerConfig.instance("d18ea3151849f0027e51a66601be4d648f7c472c0f7e4c2cfd73672b74e19593",
				"SECa24b6bdc6cf173fb81e8b33a482fcc69a11397c36d937667000810595408e5e0"));
		return dingerConfigs;
	}

	@Override
	public Class<? extends AlgorithmHandler> algorithmHandler() {
		// 使用轮询算法
		return RoundRobinHandler.class;
	}

}