package com.art.ai.manager;

import com.art.ai.core.convert.AiAppConvert;
import com.art.ai.core.dto.AiAppDTO;
import com.art.ai.core.dto.AiAppPageDTO;
import com.art.ai.dao.dataobject.AiAppDO;
import com.art.ai.dao.mysql.AiAppMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fxz
 * @date 2025-07-25
 */
@Component
@RequiredArgsConstructor
public class AiAppManager {

	private final AiAppMapper aiAppMapper;

	/**
	 * 分页查询
	 * @param aiAppPageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<AiAppDO> pageAiApp(AiAppPageDTO aiAppPageDTO) {
		LambdaQueryWrapper<AiAppDO> wrapper = Wrappers.<AiAppDO>lambdaQuery()
			.like(StringUtils.isNotBlank(aiAppPageDTO.getName()), AiAppDO::getName, aiAppPageDTO.getName())
			.eq(StringUtils.isNotBlank(aiAppPageDTO.getMode()), AiAppDO::getMode, aiAppPageDTO.getMode())
			.eq(StringUtils.isNotBlank(aiAppPageDTO.getStatus()), AiAppDO::getStatus, aiAppPageDTO.getStatus())
			.eq(aiAppPageDTO.getTenantId() != null, AiAppDO::getTenantId, aiAppPageDTO.getTenantId());
		return aiAppMapper.selectPage(Page.of(aiAppPageDTO.getCurrent(), aiAppPageDTO.getSize()), wrapper);
	}

	/**
	 * 列出所有
	 * @return 所有aiAppDO
	 */
	public List<AiAppDO> listAiApp() {
		return aiAppMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiAppById(Long id) {
		return aiAppMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param aiAppDTO aiAppDTO
	 * @return 影响条数
	 */
	public Integer updateAiAppById(AiAppDTO aiAppDTO) {
		return aiAppMapper.updateById(AiAppConvert.INSTANCE.convert(aiAppDTO));
	}

	/**
	 * 新增
	 * @param aiAppDTO aiAppDTO
	 * @return 影响条数
	 */
	public Integer addAiApp(AiAppDTO aiAppDTO) {
		return aiAppMapper.insert(AiAppConvert.INSTANCE.convert(aiAppDTO));
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return AiAppDO
	 */
	public AiAppDO findById(Long id) {
		return aiAppMapper.selectById(id);
	}

}