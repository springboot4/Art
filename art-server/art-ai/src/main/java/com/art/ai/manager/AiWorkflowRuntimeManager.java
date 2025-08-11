package com.art.ai.manager;

import com.art.ai.core.convert.AiWorkflowRuntimeConvert;
import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowRuntimePageDTO;
import com.art.ai.dao.dataobject.AiWorkflowRuntimeDO;
import com.art.ai.dao.mysql.AiWorkflowRuntimeMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fxz
 * @date 2025-08-09
 */
@Component
@RequiredArgsConstructor
public class AiWorkflowRuntimeManager {

	private final AiWorkflowRuntimeMapper aiWorkflowRuntimeMapper;

	/**
	 * 分页查询
	 * @param aiWorkflowRuntimePageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<AiWorkflowRuntimeDO> pageAiWorkflowRuntime(AiWorkflowRuntimePageDTO aiWorkflowRuntimePageDTO) {
		return aiWorkflowRuntimeMapper.selectPage(
				Page.of(aiWorkflowRuntimePageDTO.getCurrent(), aiWorkflowRuntimePageDTO.getSize()),
				Wrappers.emptyWrapper());
	}

	/**
	 * 列出所有
	 * @return 所有aiWorkflowRuntimeDO
	 */
	public List<AiWorkflowRuntimeDO> listAiWorkflowRuntime() {
		return aiWorkflowRuntimeMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiWorkflowRuntimeById(Long id) {
		return aiWorkflowRuntimeMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param aiWorkflowRuntimeDTO aiWorkflowRuntimeDTO
	 * @return 影响条数
	 */
	public Integer updateAiWorkflowRuntimeById(AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO) {
		return aiWorkflowRuntimeMapper.updateById(AiWorkflowRuntimeConvert.INSTANCE.convert(aiWorkflowRuntimeDTO));
	}

	/**
	 * 新增
	 * @param aiWorkflowRuntimeDTO aiWorkflowRuntimeDTO
	 * @return 影响条数
	 */
	public AiWorkflowRuntimeDTO addAiWorkflowRuntime(AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO) {
		AiWorkflowRuntimeDO runtimeDO = AiWorkflowRuntimeConvert.INSTANCE.convert(aiWorkflowRuntimeDTO);
		aiWorkflowRuntimeMapper.insert(runtimeDO);
		return AiWorkflowRuntimeConvert.INSTANCE.convert(runtimeDO);
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return AiWorkflowRuntimeDO
	 */
	public AiWorkflowRuntimeDO findById(Long id) {
		return aiWorkflowRuntimeMapper.selectById(id);
	}

}