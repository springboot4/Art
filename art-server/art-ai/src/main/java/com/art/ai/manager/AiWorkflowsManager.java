package com.art.ai.manager;

import com.art.ai.core.convert.AiWorkflowsConvert;
import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.core.dto.AiWorkflowsPageDTO;
import com.art.ai.dao.dataobject.AiWorkflowsDO;
import com.art.ai.dao.mysql.AiWorkflowsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author fxz
 * @date 2025-07-31
 */
@Component
@RequiredArgsConstructor
public class AiWorkflowsManager {

	private final AiWorkflowsMapper aiWorkflowsMapper;

	/**
	 * 分页查询
	 * @param aiWorkflowsPageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<AiWorkflowsDO> pageAiWorkflows(AiWorkflowsPageDTO aiWorkflowsPageDTO) {
		return aiWorkflowsMapper.selectPage(Page.of(aiWorkflowsPageDTO.getCurrent(), aiWorkflowsPageDTO.getSize()),
				Wrappers.emptyWrapper());
	}

	/**
	 * 列出所有
	 * @return 所有aiWorkflowsDO
	 */
	public List<AiWorkflowsDO> listAiWorkflows() {
		return aiWorkflowsMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiWorkflowsById(Long id) {
		return aiWorkflowsMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param aiWorkflowsDTO aiWorkflowsDTO
	 * @return 影响条数
	 */
	public Integer updateAiWorkflowsById(AiWorkflowsDTO aiWorkflowsDTO) {
		return aiWorkflowsMapper.updateById(AiWorkflowsConvert.INSTANCE.convert(aiWorkflowsDTO));
	}

	/**
	 * 新增
	 * @param aiWorkflowsDTO aiWorkflowsDTO
	 * @return 影响条数
	 */
	public Integer addAiWorkflows(AiWorkflowsDTO aiWorkflowsDTO) {
		return aiWorkflowsMapper.insert(AiWorkflowsConvert.INSTANCE.convert(aiWorkflowsDTO));
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return AiWorkflowsDO
	 */
	public AiWorkflowsDO findById(Long id) {
		return aiWorkflowsMapper.selectById(id);
	}

	public AiWorkflowsDO findByWrapper(AiWorkflowsDTO aiWorkflowsDTO) {
		LambdaQueryWrapper<AiWorkflowsDO> wrapper = Wrappers.<AiWorkflowsDO>lambdaQuery()
			.eq(Objects.nonNull(aiWorkflowsDTO.getAppId()), AiWorkflowsDO::getAppId, aiWorkflowsDTO.getAppId())
			.eq(StringUtils.isNotBlank(aiWorkflowsDTO.getType()), AiWorkflowsDO::getType, aiWorkflowsDTO.getType())
			.eq(StringUtils.isNotBlank(aiWorkflowsDTO.getVersion()), AiWorkflowsDO::getVersion,
					aiWorkflowsDTO.getVersion())
			.orderByDesc(AiWorkflowsDO::getUpdateTime)
			.last("limit 1");
		return aiWorkflowsMapper.selectOne(wrapper);
	}

	/**
	 * 保存或者更新
	 */
	public Integer saveOrUpdate(AiWorkflowsDTO aiWorkflowsDTO) {
		AiWorkflowsDO entity = AiWorkflowsConvert.INSTANCE.convert(aiWorkflowsDTO);
		if (Objects.nonNull(entity.getId())) {
			return aiWorkflowsMapper.updateById(entity);
		}

		return addAiWorkflows(aiWorkflowsDTO);
	}

}