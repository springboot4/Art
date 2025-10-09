package com.art.ai.manager;

import com.art.ai.core.convert.AiModelConvert;
import com.art.ai.core.dto.AiModelDTO;
import com.art.ai.core.dto.AiModelPageDTO;
import com.art.ai.dao.dataobject.AiModelDO;
import com.art.ai.dao.mysql.AiModelMapper;
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
public class AiModelManager {

	private final AiModelMapper aiModelMapper;

	/**
	 * 分页查询
	 * @param aiModelPageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<AiModelDO> pageAiModel(AiModelPageDTO aiModelPageDTO) {
		return aiModelMapper.selectPage(Page.of(aiModelPageDTO.getCurrent(), aiModelPageDTO.getSize()), Wrappers
			.<AiModelDO>lambdaQuery()
			.eq(aiModelPageDTO.getPlatform() != null, AiModelDO::getPlatform, aiModelPageDTO.getPlatform())
			.like(StringUtils.isNotBlank(aiModelPageDTO.getName()), AiModelDO::getName, aiModelPageDTO.getName())
			.eq(StringUtils.isNotBlank(aiModelPageDTO.getType()), AiModelDO::getType, aiModelPageDTO.getType())
			.eq(aiModelPageDTO.getEnable() != null, AiModelDO::getEnable, aiModelPageDTO.getEnable())
			.orderByDesc(AiModelDO::getId));
	}

	/**
	 * 列出所有
	 * @return 所有aiModelDO
	 */
	public List<AiModelDO> listAiModel() {
		return aiModelMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiModelById(Long id) {
		return aiModelMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param aiModelDTO aiModelDTO
	 * @return 影响条数
	 */
	public Integer updateAiModelById(AiModelDTO aiModelDTO) {
		return aiModelMapper.updateById(AiModelConvert.INSTANCE.convert(aiModelDTO));
	}

	/**
	 * 新增
	 * @param aiModelDTO aiModelDTO
	 * @return 影响条数
	 */
	public Integer addAiModel(AiModelDTO aiModelDTO) {
		return aiModelMapper.insert(AiModelConvert.INSTANCE.convert(aiModelDTO));
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return AiModelDO
	 */
	public AiModelDO findById(Long id) {
		return aiModelMapper.selectById(id);
	}

}