package com.art.ai.service.dataset.rag.embedding.entity;

import com.art.ai.service.dataset.rag.constant.KnowledgeConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author fxz
 * @since 2025/9/15 13:16
 */
@AllArgsConstructor
@Builder
@Getter
public class DocumentSplitterConfig {

	@Builder.Default
	private int maxSegmentSizeInTokens = KnowledgeConstants.MAX_SEGMENT_SIZE_IN_TOKENS;

	@Builder.Default
	private int maxOverlapSizeInTokens = KnowledgeConstants.MAX_OVERLAP_SIZE_IN_TOKENS;

}
