package com.art.ai.service.dataset.service;

import cn.hutool.core.io.FileUtil;
import com.art.ai.core.convert.AiDatasetsConvert;
import com.art.ai.core.convert.AiDocumentsConvert;
import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.core.dto.dataset.AiDatasetsPageDTO;
import com.art.ai.core.dto.dataset.AiDatasetsUploadDocDTO;
import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.dao.dataobject.AiDatasetsDO;
import com.art.ai.manager.AiDatasetsManager;
import com.art.ai.manager.AiDocumentsManager;
import com.art.ai.service.dataset.rag.constant.EmbeddingStatusEnum;
import com.art.ai.service.dataset.rag.constant.GraphStatusEnum;
import com.art.ai.service.dataset.rag.file.DocumentLoaderFactory;
import com.art.ai.service.document.AiDocumentsService;
import com.art.core.common.exception.ArtException;
import com.art.core.common.model.ResultOpt;
import com.art.system.api.file.FileServiceApi;
import com.art.system.api.file.dto.FileDownloadDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.langchain4j.data.document.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDatasetsServiceImpl implements AiDatasetsService {

	private final AiDatasetsManager aiDatasetsManager;

	private final AiDocumentsManager aiDocumentsManager;

	private final FileServiceApi fileServiceApi;

	private final AiDocumentsService aiDocumentsService;

	/**
	 * 创建数据集
	 */
	@Override
	public AiDatasetsDTO addAiDatasets(AiDatasetsDTO aiDatasetsDTO) {
		return aiDatasetsManager.addAiDatasets(aiDatasetsDTO);
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiDatasets(AiDatasetsDTO aiDatasetsDTO) {
		return aiDatasetsManager.updateAiDatasetsById(aiDatasetsDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiDatasetsDTO> pageAiDatasets(AiDatasetsPageDTO aiDatasetsPageDTO) {
		return AiDatasetsConvert.INSTANCE.convertPage(aiDatasetsManager.pageAiDatasets(aiDatasetsPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiDatasetsDTO findById(Long id) {
		return AiDatasetsConvert.INSTANCE.convert(aiDatasetsManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiDatasetsDTO> findAll() {
		return AiDatasetsConvert.INSTANCE.convertList(aiDatasetsManager.listAiDatasets());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiDatasets(Long id) {
		return aiDatasetsManager.deleteAiDatasetsById(id) > 0;
	}

	/**
	 * 数据集文档上传
	 */
	@Override
	public Boolean uploadDoc(AiDatasetsUploadDocDTO uploadDoc) {
		try {
			Long datasetsId = uploadDoc.getDatasetsId();

			// todo fxz 数据集权限校验
			AiDatasetsDO datasetsDO = aiDatasetsManager.findById(datasetsId);
			AiDatasetsDTO datasetsDTO = AiDatasetsConvert.INSTANCE.convert(datasetsDO);

			// 加载文件信息
			String fileUrl = ResultOpt.of(fileServiceApi.preSignUploadUrl(
					new FileDownloadDTO().setFileName(uploadDoc.getFileName()).setBucket(uploadDoc.getBucketName())))
				.getData()
				.orElseThrow(() -> new ArtException("文件不存在"));

			// todo fxz 文件类型
			Document document = DocumentLoaderFactory.loadDocument(fileUrl, FileUtil.extName(uploadDoc.getFileName()))
				.orElseThrow(() -> new ArtException("不支持的文件类型"));

			AiDocumentsDTO documentsDTO = new AiDocumentsDTO();
			documentsDTO.setDatasetId(datasetsId);
			documentsDTO.setContent(document.text());
			documentsDTO.setFileName(uploadDoc.getFileName());
			documentsDTO.setTitle(uploadDoc.getFileName());
			documentsDTO.setBucketName(uploadDoc.getBucketName());
			documentsDTO.setOriginalFilename(uploadDoc.getOriginalFilename());
			documentsDTO.setGraphicalStatus(GraphStatusEnum.NONE);
			documentsDTO.setEmbeddingStatus(EmbeddingStatusEnum.NONE);
			documentsDTO = AiDocumentsConvert.INSTANCE.convert(aiDocumentsManager.addAiDocuments(documentsDTO));

			aiDocumentsService.asyncIndex(datasetsDTO, documentsDTO, uploadDoc.getIndexTypes());

			return Boolean.TRUE;
		}
		catch (Exception e) {
			log.error("uploadDoc error", e);
			return Boolean.FALSE;
		}
	}

}