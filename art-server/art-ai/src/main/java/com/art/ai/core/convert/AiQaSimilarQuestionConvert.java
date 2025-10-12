package com.art.ai.core.convert;

import com.art.ai.core.dto.qa.AiQaSimilarQuestionDTO;
import com.art.ai.dao.dataobject.AiQaSimilarQuestionsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * QA相似问题转换器
 *
 * @author fxz
 * @since 2025/10/12
 */
@Mapper
public interface AiQaSimilarQuestionConvert {

	AiQaSimilarQuestionConvert INSTANCE = Mappers.getMapper(AiQaSimilarQuestionConvert.class);

	List<AiQaSimilarQuestionDTO> convertList(List<AiQaSimilarQuestionsDO> list);

	AiQaSimilarQuestionDTO convert(AiQaSimilarQuestionsDO dataObject);

	AiQaSimilarQuestionsDO convert(AiQaSimilarQuestionDTO dto);

}
