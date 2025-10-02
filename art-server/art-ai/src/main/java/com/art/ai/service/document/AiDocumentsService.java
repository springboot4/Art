package com.art.ai.service.document;

import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.core.dto.document.AiDocumentsPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @date 2025-09-09
 */
public interface AiDocumentsService {

	/**
	 * 添加
	 */
	Boolean addAiDocuments(AiDocumentsDTO aiDocumentsDTO);

	/**
	 * 修改
	 */
	Boolean updateAiDocuments(AiDocumentsDTO aiDocumentsDTO);

	/**
	 * 分页
	 */
	IPage<AiDocumentsDTO> pageAiDocuments(AiDocumentsPageDTO aiDocumentsPageDTO);

	/**
	 * 获取单条
	 */
	AiDocumentsDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<AiDocumentsDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiDocuments(Long id);

	/**
	 * 查询文档的图谱信息
	 */
	Map<String, Object> queryDocumentGraphInfo(Long documentId);

}