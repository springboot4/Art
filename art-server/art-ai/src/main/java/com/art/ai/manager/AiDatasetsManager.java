package com.art.ai.manager;

import com.art.ai.core.convert.AiDatasetsConvert;
import com.art.ai.core.dto.AiDatasetsDTO;
import com.art.ai.core.dto.AiDatasetsPageDTO;
import com.art.ai.dao.dataobject.AiDatasetsDO;
import com.art.ai.dao.mysql.AiDatasetsMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Component
@RequiredArgsConstructor
public class AiDatasetsManager {

	private final AiDatasetsMapper aiDatasetsMapper;

	/**
	 * 分页查询
	 * @param aiDatasetsPageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<AiDatasetsDO> pageAiDatasets(AiDatasetsPageDTO aiDatasetsPageDTO) {
		return aiDatasetsMapper.selectPage(Page.of(aiDatasetsPageDTO.getCurrent(), aiDatasetsPageDTO.getSize()),
				Wrappers.emptyWrapper());
	}

	/**
	 * 列出所有
	 * @return 所有aiDatasetsDO
	 */
	public List<AiDatasetsDO> listAiDatasets() {
		return aiDatasetsMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiDatasetsById(Long id) {
		return aiDatasetsMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param aiDatasetsDTO aiDatasetsDTO
	 * @return 影响条数
	 */
	public Integer updateAiDatasetsById(AiDatasetsDTO aiDatasetsDTO) {
		return aiDatasetsMapper.updateById(AiDatasetsConvert.INSTANCE.convert(aiDatasetsDTO));
	}

	/**
	 * 新增
	 * @param aiDatasetsDTO aiDatasetsDTO
	 * @return 影响条数
	 */
	public Integer addAiDatasets(AiDatasetsDTO aiDatasetsDTO) {
		return aiDatasetsMapper.insert(AiDatasetsConvert.INSTANCE.convert(aiDatasetsDTO));
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return AiDatasetsDO
	 */
	public AiDatasetsDO findById(Long id) {
		return aiDatasetsMapper.selectById(id);
	}

}