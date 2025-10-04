package com.art.ai.service.dataset.service;

import com.art.ai.core.convert.AiDocumentSegmentConvert;
import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.core.dto.document.AiDocumentSegmentPageDTO;
import com.art.ai.manager.AiDocumentSegmentManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDocumentSegmentServiceImpl implements AiDocumentSegmentService {

	private final AiDocumentSegmentManager aiDocumentSegmentManager;

	/**
	 * 添加
	 */
	@Override
	public AiDocumentSegmentDTO addAiDocumentSegment(AiDocumentSegmentDTO aiDocumentSegmentDTO) {
		return aiDocumentSegmentManager.addAiDocumentSegment(aiDocumentSegmentDTO);
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiDocumentSegment(AiDocumentSegmentDTO aiDocumentSegmentDTO) {
		return aiDocumentSegmentManager.updateAiDocumentSegmentById(aiDocumentSegmentDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiDocumentSegmentDTO> pageAiDocumentSegment(AiDocumentSegmentPageDTO aiDocumentGraphSegmentPageDTO) {
		return AiDocumentSegmentConvert.INSTANCE
			.convertPage(aiDocumentSegmentManager.pageAiDocumentSegment(aiDocumentGraphSegmentPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiDocumentSegmentDTO findById(Long id) {
		return AiDocumentSegmentConvert.INSTANCE.convert(aiDocumentSegmentManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiDocumentSegmentDTO> findAll() {
		return AiDocumentSegmentConvert.INSTANCE.convertList(aiDocumentSegmentManager.listAiDocumentSegment());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiDocumentSegment(Long id) {
		return aiDocumentSegmentManager.deleteAiDocumentSegmentById(id) > 0;
	}

	/**
	 * 根据文档ID删除
	 */
	@Override
	public Boolean deleteByDocumentId(Long documentId, @Nullable String segmentType) {
		return aiDocumentSegmentManager.deleteAiDocumentSegmentByWrapper(
				new AiDocumentSegmentDTO().setDocumentId(documentId).setSegmentType(segmentType)) > 0;
	}

}