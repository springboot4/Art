package com.art.ai.manager;

import cn.hutool.core.util.StrUtil;
import com.art.ai.dao.dataobject.AiConversationDO;
import com.art.ai.dao.mysql.AiConversationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * AI会话Manager - 数据访问层
 *
 * @author fxz
 * @date 2025-10-18
 */
@Component
@RequiredArgsConstructor
public class AiConversationManager {

	private final AiConversationMapper conversationMapper;

	/**
	 * 新增会话
	 * @param conversationDO 会话DO
	 * @return 影响行数
	 */
	public int insert(AiConversationDO conversationDO) {
		return conversationMapper.insert(conversationDO);
	}

	/**
	 * 根据ID查询
	 * @param id 主键
	 * @return 会话DO
	 */
	public AiConversationDO findById(Long id) {
		return conversationMapper.selectById(id);
	}

	/**
	 * 分页查询
	 * @param current 当前页
	 * @param size 每页大小
	 * @param appId 应用ID
	 * @param endUserId 用户ID
	 * @param status 状态
	 * @param name 会话名称（模糊查询）
	 * @return 分页结果
	 */
	public Page<AiConversationDO> page(long current, long size, Long appId, Long endUserId, String status,
			String name) {
		LambdaQueryWrapper<AiConversationDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(appId != null, AiConversationDO::getAppId, appId);
		wrapper.eq(endUserId != null, AiConversationDO::getEndUserId, endUserId);
		wrapper.eq(StrUtil.isNotBlank(status), AiConversationDO::getStatus, status);
		wrapper.like(StrUtil.isNotBlank(name), AiConversationDO::getName, name);
		wrapper.orderByDesc(AiConversationDO::getLastMessageAt);

		return conversationMapper.selectPage(Page.of(current, size), wrapper);
	}

	/**
	 * 根据ID更新
	 * @param conversationDO 会话DO
	 * @return 影响行数
	 */
	public int updateById(AiConversationDO conversationDO) {
		return conversationMapper.updateById(conversationDO);
	}

	/**
	 * 更新统计信息
	 * @param conversationId 会话ID
	 * @param messageDelta 消息增量
	 * @param tokensDelta Token增量
	 * @param costDelta 成本增量
	 * @return 影响行数
	 */
	public int updateStats(Long conversationId, int messageDelta, int tokensDelta, BigDecimal costDelta) {
		return conversationMapper.updateStats(conversationId, messageDelta, tokensDelta, costDelta);
	}

	/**
	 * 根据ID删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public int deleteById(Long id) {
		return conversationMapper.deleteById(id);
	}

}
