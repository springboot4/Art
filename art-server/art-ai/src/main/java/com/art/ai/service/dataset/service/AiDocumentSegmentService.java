package com.art.ai.service.dataset.service;

import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.core.dto.document.AiDocumentSegmentPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-14
 */
public interface AiDocumentSegmentService {

	/**
	 * 添加
	 */
	AiDocumentSegmentDTO addAiDocumentSegment(AiDocumentSegmentDTO aiDocumentSegmentDTO);

	/**
	 * 修改
	 */
	Boolean updateAiDocumentSegment(AiDocumentSegmentDTO aiDocumentSegmentDTO);

	/**
	 * 分页
	 */
	IPage<AiDocumentSegmentDTO> pageAiDocumentSegment(AiDocumentSegmentPageDTO aiDocumentGraphSegmentPageDTO);

	/**
	 * 获取单条
	 */
	AiDocumentSegmentDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<AiDocumentSegmentDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiDocumentSegment(Long id);

	/**
	 * 根据文档ID删除
	 */
	Boolean deleteByDocumentId(Long documentId);

}