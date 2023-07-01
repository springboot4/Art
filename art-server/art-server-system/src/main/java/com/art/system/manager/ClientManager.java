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

package com.art.system.manager;

import com.art.system.api.client.dto.ClientDetailsDTO;
import com.art.system.api.client.dto.ClientDetailsPageDTO;
import com.art.system.core.convert.ClientDetailsConvert;
import com.art.system.dao.dataobject.ClientDO;
import com.art.system.dao.mysql.ClientMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 终端信息表
 *
 * @author fxz
 * @date 2023-04-13
 */
@Component
@RequiredArgsConstructor
public class ClientManager {

	private final ClientMapper clientMapper;

	/**
	 * 分页查询
	 * @param clientDetailsPageDTO 分页参数
	 * @return 分页结果
	 */
	public Page<ClientDO> pageClientDetails(ClientDetailsPageDTO clientDetailsPageDTO) {
		return clientMapper.selectPage(Page.of(clientDetailsPageDTO.getCurrent(), clientDetailsPageDTO.getSize()),
				Wrappers.<ClientDO>lambdaQuery()
					.like(StringUtils.isNotBlank(clientDetailsPageDTO.getClientId()), ClientDO::getClientId,
							clientDetailsPageDTO.getClientId()));
	}

	/**
	 * 列出所有
	 * @return 所有ClientDetailsDO
	 */
	public List<ClientDO> listClientDetails() {
		return clientMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 根据Id删除
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteClientDetailsById(String id) {
		return clientMapper.deleteById(id);
	}

	/**
	 * 根据id更新
	 * @param clientDetailsDTO ClientDetailsDTO
	 * @return 影响条数
	 */
	public Integer updateClientDetailsById(ClientDetailsDTO clientDetailsDTO) {
		return clientMapper.updateById(ClientDetailsConvert.INSTANCE.convert(clientDetailsDTO));
	}

	/**
	 * 新增
	 * @param clientDetailsDTO clientDetailsDTO
	 * @return 影响条数
	 */
	public Integer addClientDetails(ClientDetailsDTO clientDetailsDTO) {
		return clientMapper.insert(ClientDetailsConvert.INSTANCE.convert(clientDetailsDTO));
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return ClientDO
	 */
	public ClientDO findById(String id) {
		return clientMapper.selectById(id);
	}

}