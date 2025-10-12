package com.art.ai.manager;

import com.art.ai.dao.dataobject.AiQaHitLogsDO;
import com.art.ai.dao.mysql.AiQaHitLogsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * QA命中日志Manager
 *
 * @author fxz
 * @since 2025/10/12
 */
@Component
@RequiredArgsConstructor
public class AiQaHitLogsManager {

	private final AiQaHitLogsMapper hitLogsMapper;

	/**
	 * 新增命中日志
	 */
	public int insert(AiQaHitLogsDO hitLog) {
		return hitLogsMapper.insert(hitLog);
	}

	/**
	 * 根据ID查询
	 */
	public AiQaHitLogsDO findById(Long id) {
		return hitLogsMapper.selectById(id);
	}

}
