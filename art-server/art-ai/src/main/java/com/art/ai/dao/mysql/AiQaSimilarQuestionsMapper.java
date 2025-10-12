package com.art.ai.dao.mysql;

import com.art.ai.dao.dataobject.AiQaSimilarQuestionsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * QA相似问题Mapper
 *
 * @author fxz
 * @since 2025/10/12
 */
@Mapper
public interface AiQaSimilarQuestionsMapper extends BaseMapper<AiQaSimilarQuestionsDO> {

	/**
	 * 根据相似问题Hash查询
	 */
	@Select("SELECT s.* FROM ai_qa_similar_questions s " + "INNER JOIN ai_qa_pairs p ON s.qa_pair_id = p.id "
			+ "WHERE s.similar_hash = #{similarHash} AND p.enabled = 1")
	List<AiQaSimilarQuestionsDO> findByHashWithEnabledQa(@Param("similarHash") String similarHash);

}
