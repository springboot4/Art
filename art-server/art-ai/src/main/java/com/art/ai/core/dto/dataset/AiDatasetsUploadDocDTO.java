package com.art.ai.core.dto.dataset;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author fxz
 * @since 2025/9/9 13:21
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "")
@Data
public class AiDatasetsUploadDocDTO {

	@Schema(description = "数据集id")
	private Long datasetsId;

	@Schema(description = "索引类型")
	private String indexTypes;

	@Schema(description = "文件名称")
	private String fileName;

	@Schema(description = "桶名称")
	private String bucketName;

}
