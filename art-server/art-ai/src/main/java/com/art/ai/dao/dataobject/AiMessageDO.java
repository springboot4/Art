package com.art.ai.dao.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI消息实体
 *
 * @author fxz
 * @date 2025-10-18
 */
@Data
@TableName("ai_messages")
public class AiMessageDO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 消息UUID（对外暴露）
	 */
	private String messageUuid;

	/**
	 * 所属会话ID
	 */
	private Long conversationId;

	/**
	 * 实例ID（工作流或Agent实例，预留字段）
	 */
	private Long instanceId;

	/**
	 * 实例类型：workflow-工作流, agent-智能体（预留字段）
	 */
	private String instanceType;

	/**
	 * 角色：user-用户, assistant-助手, system-系统
	 */
	private String role;

	/**
	 * 消息类型：text-文本, image-图片, file-文件, audio-音频, video-视频
	 */
	private String messageType;

	/**
	 * 消息内容
	 */
	private String content;

	/**
	 * 模型提供商（如openai）
	 */
	private String modelProvider;

	/**
	 * 模型ID（如gpt-4）
	 */
	private String modelId;

	/**
	 * 输入Token数
	 */
	private Integer promptTokens;

	/**
	 * 输出Token数
	 */
	private Integer completionTokens;

	/**
	 * 总Token数
	 */
	private Integer totalTokens;

	/**
	 * 总成本
	 */
	private BigDecimal totalCost;

	/**
	 * 状态：pending-待处理, completed-已完成, failed-失败
	 */
	private String status;

	/**
	 * 错误信息
	 */
	private String errorMessage;

	/**
	 * 扩展元数据（JSON格式）
	 */
	private String metadata;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 完成时间
	 */
	private LocalDateTime completedAt;

}
