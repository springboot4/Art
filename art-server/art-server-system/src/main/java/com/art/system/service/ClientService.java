/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.system.service;

import com.art.system.api.client.dto.ClientDetailsDTO;
import com.art.system.api.client.dto.ClientDetailsPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 终端信息表
 *
 * @author fxz
 * @date 2023-04-13
 */
public interface ClientService {

	/**
	 * 添加
	 */
	Boolean addClientDetails(ClientDetailsDTO clientDetailsDTO);

	/**
	 * 修改
	 */
	Boolean updateClientDetails(ClientDetailsDTO clientDetailsDTO);

	/**
	 * 分页
	 */
	IPage<ClientDetailsDTO> pageClientDetails(ClientDetailsPageDTO clientDetailsPageDTO);

	/**
	 * 获取单条
	 */
	ClientDetailsDTO findById(String id);

	/**
	 * 获取全部
	 */
	List<ClientDetailsDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteClientDetails(String id);

}