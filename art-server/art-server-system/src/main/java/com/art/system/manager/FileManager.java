/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.system.manager;

import com.art.system.api.file.dto.FileDTO;
import com.art.system.api.file.dto.FilePageDTO;
import com.art.system.core.convert.FileConvert;
import com.art.system.dao.dataobject.FileDO;
import com.art.system.dao.mysql.FileMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 18:54
 */
@Component
@RequiredArgsConstructor
public class FileManager {

	private final FileMapper fileMapper;

	public void saveFile(FileDO fileDO) {
		fileMapper.insert(fileDO);
	}

	public Integer updateFileById(FileDTO fileDto) {
		return fileMapper.updateById(FileConvert.INSTANCE.convert(fileDto));
	}

	public FileDO getFileById(Long id) {
		return fileMapper.selectById(id);
	}

	public Integer deleteFileById(Long id) {
		return fileMapper.deleteById(id);
	}

	public List<FileDO> listFile() {
		return fileMapper.selectList(Wrappers.emptyWrapper());
	}

	public Page<FileDO> pageFile(FilePageDTO filePageDTO) {
		return fileMapper
			.selectPage(Page.of(filePageDTO.getCurrent(), filePageDTO.getSize()), Wrappers.<FileDO>lambdaQuery()
				.like(StringUtils.isNotBlank(filePageDTO.getFileName()), FileDO::getFileName, filePageDTO.getFileName())
				.like(StringUtils.isNotBlank(filePageDTO.getOriginal()), FileDO::getOriginal, filePageDTO.getOriginal())
				.like(StringUtils.isNotBlank(filePageDTO.getBucketName()), FileDO::getBucketName,
						filePageDTO.getBucketName()));
	}

}
