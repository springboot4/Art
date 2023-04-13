/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.system.service.impl;

import com.art.system.api.client.dto.ClientDetailsDTO;
import com.art.system.api.client.dto.ClientDetailsPageDTO;
import com.art.system.core.convert.ClientDetailsConvert;
import com.art.system.dao.dataobject.ClientDO;
import com.art.system.manager.ClientManager;
import com.art.system.service.ClientService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 终端信息表
 *
 * @author fxz
 * @date 2023-04-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientManager clientManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addClientDetails(ClientDetailsDTO clientDetailsDTO) {
		return clientManager.addClientDetails(clientDetailsDTO) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateClientDetails(ClientDetailsDTO clientDetailsDTO) {
		return clientManager.updateClientDetailsById(clientDetailsDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<ClientDetailsDTO> pageClientDetails(ClientDetailsPageDTO dto) {
		return ClientDetailsConvert.INSTANCE.convertPage(clientManager.pageClientDetails(dto));
	}

	/**
	 * 获取单条
	 */
	@Override
	public ClientDetailsDTO findById(String id) {
		ClientDO aDo = clientManager.findById(id);
		return ClientDetailsConvert.INSTANCE.convert(aDo);
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<ClientDetailsDTO> findAll() {
		return ClientDetailsConvert.INSTANCE.convertList(clientManager.listClientDetails());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteClientDetails(String id) {
		return clientManager.deleteClientDetailsById(id) > 0;
	}

}