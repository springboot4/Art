package com.art.ai.dao.mysql;

import com.art.ai.dao.dataobject.AiQaHitLogsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * QA命中日志Mapper
 *
 * @author fxz
 * @since 2025/10/12
 */
@Mapper
public interface AiQaHitLogsMapper extends BaseMapper<AiQaHitLogsDO> {

}
