package com.art.ai.manager;

import com.art.ai.core.constants.DatasetsDataSourceConstants;
import com.art.ai.core.constants.DatasetsPermissionConstants;
import com.art.ai.core.convert.AiDatasetsConvert;
import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.core.dto.dataset.AiDatasetsPageDTO;
import com.art.ai.dao.dataobject.AiDatasetsDO;
import com.art.ai.dao.mysql.AiDatasetsMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
				Wrappers.lambdaQuery(AiDatasetsDO.class)
					.like(StringUtils.isNotBlank(aiDatasetsPageDTO.getName()), AiDatasetsDO::getName,
							aiDatasetsPageDTO.getName()));
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
	 * @return 影响条数
	 */
	public AiDatasetsDTO addAiDatasets(AiDatasetsDTO aiDatasetsDTO) {
		AiDatasetsDO datasetsDO = AiDatasetsConvert.INSTANCE.convert(aiDatasetsDTO);
		if (StringUtils.isBlank(datasetsDO.getName())) {
			datasetsDO.setName(AiDatasetsDO.newDefaultName());
		}
		if (StringUtils.isBlank(datasetsDO.getPermission())) {
			datasetsDO.setPermission(DatasetsPermissionConstants.PUBLIC);
		}
		if (StringUtils.isBlank(datasetsDO.getDataSourceType())) {
			datasetsDO.setDataSourceType(DatasetsDataSourceConstants.LOCAL);
		}
		if (StringUtils.isBlank(datasetsDO.getDescription())) {
			datasetsDO.setDescription("知识库创建于" + LocalDateTime.now());
		}

		aiDatasetsMapper.insert(datasetsDO);

		return AiDatasetsConvert.INSTANCE.convert(datasetsDO);
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