package com.art.ai.service.agent.runtime.strategy;

import com.art.ai.service.agent.runtime.AgentRunResult;
import com.art.ai.service.agent.tool.AgentToolException;

/**
 * Agent 执行策略接口 不同的策略类型（REACT、PLAN_EXECUTE）有不同的实现
 *
 * @author fxz
 */
public interface AgentStrategy {

	/**
	 * 执行agent
	 * @param context 策略执行上下文
	 * @return 执行结果
	 * @throws AgentToolException 工具执行异常
	 */
	AgentRunResult execute(AgentStrategyContext context) throws AgentToolException;

	/**
	 * 获取策略类型
	 */
	String getStrategyType();

}
