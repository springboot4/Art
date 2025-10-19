package com.art.ai.manager;

import cn.hutool.core.util.StrUtil;
import com.art.ai.dao.dataobject.AiMessageDO;
import com.art.ai.dao.mysql.AiMessageMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI消息Manager - 数据访问层
 *
 * @author fxz
 * @date 2025-10-18
 */
@Component
@RequiredArgsConstructor
public class AiMessageManager {

	private final AiMessageMapper messageMapper;

	/**
	 * 新增消息
	 * @param messageDO 消息DO
	 * @return 影响行数
	 */
	public int insert(AiMessageDO messageDO) {
		return messageMapper.insert(messageDO);
	}

	/**
	 * 根据ID查询
	 * @param id 主键
	 * @return 消息DO
	 */
	public AiMessageDO findById(Long id) {
		return messageMapper.selectById(id);
	}

	/**
	 * 查询最近N条消息
	 * @param conversationId 会话ID
	 * @param limit 数量限制
	 * @return 消息列表（按时间正序）
	 */
	public List<AiMessageDO> selectRecentMessages(Long conversationId, int limit) {
		return messageMapper.selectRecentMessages(conversationId, limit);
	}

	/**
	 * 分页查询
	 * @param current 当前页
	 * @param size 每页大小
	 * @param conversationId 会话ID
	 * @param role 角色
	 * @param status 状态
	 * @return 分页结果
	 */
	public Page<AiMessageDO> page(long current, long size, Long conversationId, String role, String status) {
		LambdaQueryWrapper<AiMessageDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(conversationId != null, AiMessageDO::getConversationId, conversationId);
		wrapper.eq(StrUtil.isNotBlank(role), AiMessageDO::getRole, role);
		wrapper.eq(StrUtil.isNotBlank(status), AiMessageDO::getStatus, status);
		wrapper.orderByAsc(AiMessageDO::getCreateTime);

		return messageMapper.selectPage(Page.of(current, size), wrapper);
	}

	/**
	 * 根据ID更新
	 * @param messageDO 消息DO
	 * @return 影响行数
	 */
	public int updateById(AiMessageDO messageDO) {
		return messageMapper.updateById(messageDO);
	}

	/**
	 * 根据ID删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public int deleteById(Long id) {
		return messageMapper.deleteById(id);
	}

}
