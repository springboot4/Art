package com.art.ai.service.dataset.rag.graph.ingest;

import com.art.ai.service.dataset.rag.graph.entity.GraphEntityDesc;
import com.art.ai.service.dataset.rag.graph.entity.GraphEntityRelation;
import com.art.ai.service.dataset.rag.graph.entity.GraphExtractionInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.GRAPH_RECORD_DELIMITER;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.TUPLE_DELIMITER;

/**
 * @author fxz
 */
@Component
public class GraphExtractionParser {

	public List<GraphExtractionInfo> parse(String response) {
		if (StringUtils.isBlank(response)) {
			return List.of();
		}

		String[] rows = StringUtils.split(response, GRAPH_RECORD_DELIMITER);
		if (ArrayUtils.isEmpty(rows)) {
			return List.of();
		}

		return Arrays.stream(rows).map(this::parseRow).filter(Objects::nonNull).collect(Collectors.toList());
	}

	public GraphExtractionInfo parseRow(String row) {
		String[] recordAttributes = StringUtils.split(row, TUPLE_DELIMITER);
		boolean entityDesc = recordAttributes.length >= 4
				&& (recordAttributes[0].contains("entity") || recordAttributes[0].contains("实体"));
		if (entityDesc) {
			String entityName = recordAttributes[1].toUpperCase();
			String entityType = recordAttributes[2].toUpperCase();
			String entityDescription = recordAttributes[3];
			return new GraphExtractionInfo(new GraphEntityDesc(entityName, entityType, entityDescription), null);
		}

		boolean entityRelation = recordAttributes.length >= 4
				&& (recordAttributes[0].contains("relationship") || recordAttributes[0].contains("关系"));
		if (entityRelation) {
			String sourceName = recordAttributes[1].toUpperCase();
			String targetName = recordAttributes[2].toUpperCase();
			String edgeDescription = recordAttributes[3];
			String weight = "1.0";
			if (recordAttributes.length > 4) {
				weight = recordAttributes[recordAttributes.length - 1];
			}
			return new GraphExtractionInfo(null,
					new GraphEntityRelation(sourceName, targetName, edgeDescription, weight));
		}

		return null;
	}

}
