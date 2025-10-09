package com.art.ai.manager;

import com.art.ai.core.convert.AiModelPlatformConvert;
import com.art.ai.core.dto.AiModelPlatformDTO;
import com.art.ai.core.dto.AiModelPlatformPageDTO;
import com.art.ai.dao.dataobject.AiModelPlatformDO;
import com.art.ai.dao.mysql.AiModelPlatformMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fxz
 * @date 2025-10-09
 */
@Component
@RequiredArgsConstructor
public class AiModelPlatformManager {

	private final AiModelPlatformMapper aiModelPlatformMapper;

	/**
	 * 分页查询
	 * @param aiModelPlatformPageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<AiModelPlatformDO> pageAiModelPlatform(AiModelPlatformPageDTO aiModelPlatformPageDTO) {
		return aiModelPlatformMapper.selectPage(
				Page.of(aiModelPlatformPageDTO.getCurrent(), aiModelPlatformPageDTO.getSize()),
				Wrappers.<AiModelPlatformDO>lambdaQuery()
					.like(StringUtils.isNotBlank(aiModelPlatformPageDTO.getName()), AiModelPlatformDO::getName,
							aiModelPlatformPageDTO.getName())
					.orderByDesc(AiModelPlatformDO::getId));
	}

	/**
	 * 列出所有
	 * @return 所有aiModelPlatformDO
	 */
	public List<AiModelPlatformDO> listAiModelPlatform() {
		return aiModelPlatformMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiModelPlatformById(Long id) {
		return aiModelPlatformMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param aiModelPlatformDTO aiModelPlatformDTO
	 * @return 影响条数
	 */
	public Integer updateAiModelPlatformById(AiModelPlatformDTO aiModelPlatformDTO) {
		return aiModelPlatformMapper.updateById(AiModelPlatformConvert.INSTANCE.convert(aiModelPlatformDTO));
	}

	/**
	 * 新增
	 * @param aiModelPlatformDTO aiModelPlatformDTO
	 * @return 影响条数
	 */
	public Integer addAiModelPlatform(AiModelPlatformDTO aiModelPlatformDTO) {
		return aiModelPlatformMapper.insert(AiModelPlatformConvert.INSTANCE.convert(aiModelPlatformDTO));
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return AiModelPlatformDO
	 */
	public AiModelPlatformDO findById(Long id) {
		return aiModelPlatformMapper.selectById(id);
	}

}