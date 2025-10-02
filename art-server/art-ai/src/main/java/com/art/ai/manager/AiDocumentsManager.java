package com.art.ai.manager;

import com.art.ai.core.convert.AiDocumentsConvert;
import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.core.dto.document.AiDocumentsPageDTO;
import com.art.ai.dao.dataobject.AiDocumentsDO;
import com.art.ai.dao.mysql.AiDocumentsMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-09
 */
@Component
@RequiredArgsConstructor
public class AiDocumentsManager {

	private final AiDocumentsMapper aiDocumentsMapper;

	/**
	 * 分页查询
	 * @param aiDocumentsPageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<AiDocumentsDO> pageAiDocuments(AiDocumentsPageDTO aiDocumentsPageDTO) {
		return aiDocumentsMapper.selectPage(Page.of(aiDocumentsPageDTO.getCurrent(), aiDocumentsPageDTO.getSize()),
				Wrappers.lambdaQuery(AiDocumentsDO.class)
					.eq(aiDocumentsPageDTO.getId() != null, AiDocumentsDO::getId, aiDocumentsPageDTO.getId())
					.eq(StringUtils.isNotBlank(aiDocumentsPageDTO.getDatasetId()), AiDocumentsDO::getDatasetId,
							aiDocumentsPageDTO.getDatasetId())
					.like(StringUtils.isNotBlank(aiDocumentsPageDTO.getFileName()), AiDocumentsDO::getFileName,
							aiDocumentsPageDTO.getFileName())
					.orderByDesc(AiDocumentsDO::getCreateTime));
	}

	/**
	 * 列出所有
	 * @return 所有aiDocumentsDO
	 */
	public List<AiDocumentsDO> listAiDocuments() {
		return aiDocumentsMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAiDocumentsById(Long id) {
		return aiDocumentsMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param aiDocumentsDTO aiDocumentsDTO
	 * @return 影响条数
	 */
	public Integer updateAiDocumentsById(AiDocumentsDTO aiDocumentsDTO) {
		return aiDocumentsMapper.updateById(AiDocumentsConvert.INSTANCE.convert(aiDocumentsDTO));
	}

	/**
	 * 新增
	 * @param aiDocumentsDTO aiDocumentsDTO
	 */
	public AiDocumentsDO addAiDocuments(AiDocumentsDTO aiDocumentsDTO) {
		AiDocumentsDO documentsDO = AiDocumentsConvert.INSTANCE.convert(aiDocumentsDTO);
		aiDocumentsMapper.insert(documentsDO);
		return documentsDO;
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return AiDocumentsDO
	 */
	public AiDocumentsDO findById(Long id) {
		return aiDocumentsMapper.selectById(id);
	}

}