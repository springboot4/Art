package com.art.ai.manager;

import com.art.ai.core.convert.AiDocumentSegmentConvert;
import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.core.dto.document.AiDocumentSegmentPageDTO;
import com.art.ai.dao.dataobject.AiDocumentSegmentDO;
import com.art.ai.dao.mysql.AiDocumentSegmentMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-14
 */
@Component
@RequiredArgsConstructor
public class AiDocumentSegmentManager {

	private final AiDocumentSegmentMapper aiDocumentSegmentMapper;

	/**
	 * 分页查询
	 * @return 分页结果
	 */
	public Page<AiDocumentSegmentDO> pageAiDocumentSegment(AiDocumentSegmentPageDTO aiDocumentSegmentPageDTO) {
		return aiDocumentSegmentMapper.selectPage(
				Page.of(aiDocumentSegmentPageDTO.getCurrent(), aiDocumentSegmentPageDTO.getSize()),
				Wrappers.lambdaQuery(AiDocumentSegmentDO.class)
					.eq(aiDocumentSegmentPageDTO.getDocumentId() != null, AiDocumentSegmentDO::getDocumentId,
							aiDocumentSegmentPageDTO.getDocumentId())
					.eq(aiDocumentSegmentPageDTO.getDatasetId() != null, AiDocumentSegmentDO::getDatasetId,
							aiDocumentSegmentPageDTO.getDatasetId())
					.orderByDesc(AiDocumentSegmentDO::getCreateTime));
	}

	/**
	 * 列出所有
	 */
	public List<AiDocumentSegmentDO> listAiDocumentSegment() {
		return aiDocumentSegmentMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiDocumentSegmentById(Long id) {
		return aiDocumentSegmentMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @return 影响条数
	 */
	public Integer updateAiDocumentSegmentById(AiDocumentSegmentDTO aiDocumentSegmentDTO) {
		return aiDocumentSegmentMapper.updateById(AiDocumentSegmentConvert.INSTANCE.convert(aiDocumentSegmentDTO));
	}

	/**
	 * 新增
	 */
	public AiDocumentSegmentDTO addAiDocumentSegment(AiDocumentSegmentDTO aiDocumentSegmentDTO) {
		AiDocumentSegmentDO segmentDO = AiDocumentSegmentConvert.INSTANCE.convert(aiDocumentSegmentDTO);
		aiDocumentSegmentMapper.insert(segmentDO);
		return AiDocumentSegmentConvert.INSTANCE.convert(segmentDO);
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 */
	public AiDocumentSegmentDO findById(Long id) {
		return aiDocumentSegmentMapper.selectById(id);
	}

	/**
	 * 根据条件删除
	 */
	public Integer deleteAiDocumentSegmentByWrapper(AiDocumentSegmentDTO aiDocumentSegmentDTO) {
		return aiDocumentSegmentMapper.delete(Wrappers.lambdaQuery(AiDocumentSegmentDO.class)
			.eq(aiDocumentSegmentDTO.getDocumentId() != null, AiDocumentSegmentDO::getDocumentId,
					aiDocumentSegmentDTO.getDocumentId())
			.eq(aiDocumentSegmentDTO.getDatasetId() != null, AiDocumentSegmentDO::getDatasetId,
					aiDocumentSegmentDTO.getDatasetId()));
	}

}