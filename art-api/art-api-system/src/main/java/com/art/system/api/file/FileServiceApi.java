package com.art.system.api.file;

import com.art.core.common.constant.ArtServerConstants;
import com.art.core.common.constant.SecurityConstants;
import com.art.core.common.model.Result;
import com.art.system.api.file.dto.FileDownloadDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author fxz
 * @since 2025/9/13 16:34
 */
@FeignClient(contextId = "fileServiceApi", value = ArtServerConstants.ART_SERVER_SYSTEM)
public interface FileServiceApi {

	@PostMapping(value = "/file/preSignUploadUrl", headers = SecurityConstants.HEADER_INNER)
	Result<String> preSignUploadUrl(@RequestBody FileDownloadDTO dto);

}
